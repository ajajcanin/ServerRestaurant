package com.example.restaurants.Dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Repository
public class ReservationDaoImpl implements ReservationDao {
    private final EntityManager entityManager;


    public ReservationDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public ResponseEntity<JsonNode> checkReservationAvailability(JsonNode json) {
        try {
            SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = json.get("date").asText().substring(0,10);
            String hour = json.get("hour").asText().substring(11,16);
            Date parsedDate = dateFromat.parse(date + ' ' + hour);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.HOUR, 1);
            if(!hour.substring(0, 2).equals("23"))
                cal.add(Calendar.DAY_OF_WEEK, 1);
            timestamp.setTime(cal.getTime().getTime());
            System.out.println(timestamp);
            List<String> bestTime = new ArrayList<String>();
            int tablesLeft = getNumTables(timestamp, bestTime, json.get("people").asInt(), json.get("idRestaurant").asLong(), true, false);

            ObjectMapper mapper = new ObjectMapper();
            ArrayNode array = mapper.createArrayNode();
            bestTime.forEach(child -> {
                array.add(child);
            });
            JsonNode res = mapper.createObjectNode();
            ((ObjectNode) res).putPOJO("bestTime", array);
            ((ObjectNode) res).put("idRestaurant", json.get("idRestaurant").asLong());
            ((ObjectNode) res).put("tablesLeft", tablesLeft);

            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (ParseException e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @Transactional
    public ResponseEntity makeReservation(JsonNode json) {

        int guests = json.get("guests").asInt();
        String date = json.get("date").asText().substring(0,10);
        SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parsedDate = null;
        try {
            parsedDate = dateFromat.parse(date + ' ' + json.get("time").asText());

            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.DAY_OF_WEEK, 1);
            timestamp.setTime(cal.getTime().getTime());
            cal.add(Calendar.HOUR, 1);
            Timestamp timestampTo = new Timestamp(cal.getTime().getTime());

            String sql = "select t.table_id " +
                "from tables t " +
                "where t.restaurant_id = :idRestaurant " +
                "and t.capacity = :guests " +
                "and not exists(select r.reservation_id " +
                "from reservations r " +
                "where t.table_id = r.table_id " +
                "and :dateTime >= r.time_from and :dateTime <= r.time_to) ";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("dateTime", timestamp);
            query.setParameter("guests", guests);
            query.setParameter("idRestaurant", json.get("idRestaurant").asLong());
            BigInteger tableId = (BigInteger)query.getSingleResult();

            String sqlUser = "select u.user_id " +
                    "from users u " +
                    "where u.email_address like :userEmail";
            Query queryUser = entityManager.createNativeQuery(sqlUser);
            queryUser.setParameter("userEmail", json.get("user").asText());
            BigInteger userId = (BigInteger)queryUser.getSingleResult();


            String sqlInsert="insert into reservations(guests, time_from, time_to, table_id, user_id) values " +
                    "(:guests, :timeFrom, :timeTo, :tableId, :userId) ";

            Query queryInsert = entityManager.createNativeQuery(sqlInsert);
            queryInsert.setParameter("userId", userId.longValue());
            queryInsert.setParameter("tableId", tableId.longValue());
            queryInsert.setParameter("guests", guests);
            queryInsert.setParameter("timeFrom", timestamp);
            queryInsert.setParameter("timeTo", timestampTo);
            queryInsert.executeUpdate();


        } catch (ParseException e) {
                    e.printStackTrace();
                }
        //query.setParameter("dateTime", people);
        return new ResponseEntity<>(HttpStatus.OK);
    }













    @Transactional
    public int getNumTables(Timestamp timestamp, List<String> bestTime, int people, long idRestaurant, boolean hit, boolean closeHit){
        String sql = "select count(t.table_id) " +
                "from tables t " +
                "where t.capacity = :people " +
                "and t.restaurant_id = :idRestaurant " +
                "and not exists(select r.reservation_id " +
                "from reservations r " +
                "where t.table_id = r.table_id " +
                "and :dateTime >= r.time_from and :dateTime <= r.time_to) ";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("people", people);
        query.setParameter("dateTime", timestamp);
        query.setParameter("idRestaurant", idRestaurant);

        BigInteger tableNum = (BigInteger) query.getSingleResult();
        System.out.println("broj stolova:" + tableNum);
        if(tableNum.intValue() > 0 && (hit || closeHit)){
            bestTime.add(timestamp.toString().substring(11,16));
            return tableNum.intValue();
        } else if(tableNum.intValue() > 0){
            bestTime.add(timestamp.toString().substring(11,16));
            IncreaseTimeByThirtyMinutes(timestamp);
            return tableNum.intValue()+getNumTables(timestamp, bestTime, people, idRestaurant, hit, true);
        } else {
            IncreaseTimeByThirtyMinutes(timestamp);
            return tableNum.intValue()+getNumTables(timestamp, bestTime, people, idRestaurant, false, closeHit);
        }
    }

    private void IncreaseTimeByThirtyMinutes(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.MINUTE, 30);
        timestamp.setTime(cal.getTime().getTime());
    }
}

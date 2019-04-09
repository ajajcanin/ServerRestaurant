package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Table;
import com.example.restaurants.Entity.Wrapper;
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
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.lang.Integer.MIN_VALUE;
import static java.lang.Math.ceil;

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
            Long idRestaurant = json.get("idRestaurant").asLong();
            int guests = json.get("people").asInt();
            SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String date = json.get("date").asText().substring(0,10);
            String hour = json.get("hour").asText().substring(11,16);
            Date parsedDate = dateFromat.parse(date + ' ' + hour);
            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.HOUR, 2);
            if(!hour.substring(0, 2).equals("23"))
                cal.add(Calendar.DAY_OF_WEEK, 1);
            timestamp.setTime(cal.getTime().getTime());
            System.out.println(timestamp);

            Timestamp tempTime = new Timestamp(cal.getTime().getTime());
            int counter = 0;

            ObjectMapper mapper = new ObjectMapper();
            JsonNode res = mapper.createObjectNode();
            ((ObjectNode) res).put("idRestaurant", idRestaurant);
            ArrayNode reservations = mapper.createArrayNode();

            while(true){
                int lengthOfStay = getDurationOfStay(tempTime, idRestaurant, guests);
                HashMap<BigInteger, Integer> freeTables = getFreeTablesInCertainTime(tempTime, lengthOfStay, idRestaurant, guests);
                List<BigInteger> tableIds = new ArrayList<>(freeTables.keySet());
                List<Integer> tableCapacities = new ArrayList<>(freeTables.values());

                Wrapper wrapper = new Wrapper(MIN_VALUE, tableCapacities, new ArrayList<>());
                System.out.println("talbeids"+ tableIds);
                System.out.println("capads"+ tableCapacities);

                getTablesForReservation(tableIds, wrapper, wrapper.getTablesCapacity().size(), new ArrayList<BigInteger>(), guests);
                Integer spaceTaken = 0;
                for(BigInteger space : wrapper.getRet()){
                    spaceTaken += freeTables.get(space);
                }
                System.out.println(spaceTaken);
                if(isSpaceAllowed(guests, spaceTaken)){
                    JsonNode tablesByTime = mapper.createObjectNode();
                    ArrayNode array = mapper.createArrayNode();
                    for(BigInteger id : wrapper.getRet()){
                        array.add(id);
                    }
                    ((ObjectNode) tablesByTime).put("bestTime", tempTime.toString().substring(11,16));
                    ((ObjectNode) tablesByTime).put("duration", lengthOfStay);
                    ((ObjectNode) tablesByTime).putPOJO("tableIds", array);
                    array.forEach(child -> {
                        System.out.println("------>"+child);
                    });
                    reservations.add(tablesByTime);
                    if(timestamp == tempTime) break;
                    else if ( counter == 2) break;
                    counter++;
                }
                IncreaseTimeByThirtyMinutes(tempTime);
                int hours = Integer.parseInt(tempTime.toString().substring(11,13));
                if(hours >= (23 - lengthOfStay)) break;
            }
            /*int tablesLeft = getNumTables(timestamp, bestTime, json.get("people").asInt(), json.get("idRestaurant").asLong(), true, false);

            ArrayNode array = mapper.createArrayNode();
            bestTime.forEach(child -> {
                array.add(child);
            });
            ((ObjectNode) res).putPOJO("bestTime", array);
            ((ObjectNode) res).put("idRestaurant", json.get("idRestaurant").asLong());
            ((ObjectNode) res).put("tablesLeft", tablesLeft);*/

            ((ObjectNode) res).putPOJO("reservations", reservations);
            return new ResponseEntity<>(res, HttpStatus.OK);

        } catch (ParseException e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    @Transactional
    public ResponseEntity makeReservation(JsonNode json) {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode tableNode;
        try {
            tableNode = mapper.readTree(String.valueOf(json)).get("tables");
            List<BigInteger> givenTables = new ArrayList<>();
            for(JsonNode node : tableNode){
                givenTables.add(BigInteger.valueOf(node.asLong()));
            }

        int guests = json.get("guests").asInt();
        String date = json.get("date").asText().substring(0,10);
        int duration = json.get("duration").asInt();
        Long idRestaurant = json.get("idRestaurant").asLong();

        SimpleDateFormat dateFromat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date parsedDate = null;
            parsedDate = dateFromat.parse(date + ' ' + json.get("time").asText());

            Timestamp timestamp = new Timestamp(parsedDate.getTime());
            Calendar cal = Calendar.getInstance();
            cal.setTime(timestamp);
            cal.add(Calendar.HOUR, duration);
            Timestamp timestampTo = new Timestamp(cal.getTime().getTime());

            System.out.println(timestamp + "drugi" + timestamp);
            HashMap<BigInteger , Integer> freeTables = getFreeTablesInCertainTime(timestamp, duration, idRestaurant, 0);
            List<BigInteger> tableIds = new ArrayList<>(freeTables.keySet());

//sve slobodne stolove

                String sqlUser = "select u.user_id " +
                        "from users u " +
                        "where u.email_address like :userEmail";
                Query queryUser = entityManager.createNativeQuery(sqlUser);
                queryUser.setParameter("userEmail", json.get("user").asText());
                BigInteger userId = (BigInteger)queryUser.getSingleResult();

            System.out.println(areGivenTablesFree(tableIds, givenTables));
            if(areGivenTablesFree(tableIds, givenTables)){

                for(BigInteger table : givenTables){

                    String sqlInsert="insert into reservations(guests, time_from, time_to, table_id, user_id) values " +
                            "(:guests, :timeFrom, :timeTo, :tableId, :userId) ";

                    Query queryInsert = entityManager.createNativeQuery(sqlInsert);
                    queryInsert.setParameter("userId", userId.longValue());
                    queryInsert.setParameter("tableId", table.longValue());
                    queryInsert.setParameter("guests", guests);
                    queryInsert.setParameter("timeFrom", timestamp);
                    queryInsert.setParameter("timeTo", timestampTo);
                    queryInsert.executeUpdate();
                }


                return new ResponseEntity<>(HttpStatus.OK);
            }



        } catch (ParseException e) {
                    e.printStackTrace();
                } catch (IOException e) {
            e.printStackTrace();
        }
        //query.setParameter("dateTime", people);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    /*
    public int getNumTables(Timestamp timestamp, List<String> bestTime, int people, long idRestaurant, boolean hit, boolean closeHit){
        String sql = "select t.table_id, t.capacity " +
                "from tables t " +
                "where t.restaurant_id = :idRestaurant " +
                "and not exists(select r.reservation_id " +
                "from reservations r " +
                "where t.table_id = r.table_id " +
                "and tsrange(r.time_from, r.time_to, '[]') && tsrange(:dateTime, :dateTimeEnd, '[]')) ";

        String sqlStay = "select rs.stay " +
                "from restaurant_stays rs " +
                "where rs.restaurant_id = :idRestaurant " +
                "and rs.meal_stay_id = (select ms.meal_stay_id" +
                                "from meal_stays ms" +
                                "where ms.start::time <= :dateTime::time " +
                                "and ms.end::time >= :dateTime::time) ";

        Query queryStay = entityManager.createNativeQuery(sqlStay);
        queryStay.setParameter("dateTime", timestamp);
        queryStay.setParameter("idRestaurant", idRestaurant);

        int mealType;
        List<Integer> stays = queryStay.getResultList();
        if(stays.isEmpty()) mealType = 1;
        else mealType = stays.get(0);
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.HOUR, mealType);
        Timestamp timestampEnd = new Timestamp(cal.getTime().getTime());

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("people", people);
        query.setParameter("dateTime", timestamp);
        query.setParameter("idRestaurant", idRestaurant);
        query.setParameter("dateTimeEnd", timestampEnd);

        LinkedHashMap<BigInteger, Integer> tables = new LinkedHashMap<>();
        List<Object[]> tableObjects = query.getResultList();
        for(Object[] result : tableObjects){
            tables.put((BigInteger) result[0], (Integer) result[1]);
        }
        System.out.println("broj stolova:" + tables.size());
        int tableNum = tables.size();
        if(tableNum > 0 && (hit || closeHit)){
            bestTime.add(timestamp.toString().substring(11,16));
            return tableNum;
        } else if(tableNum > 0){
            bestTime.add(timestamp.toString().substring(11,16));
            IncreaseTimeByThirtyMinutes(timestamp);
            return tableNum+getNumTables(timestamp, bestTime, people, idRestaurant, hit, true);
        } else {
            IncreaseTimeByThirtyMinutes(timestamp);
            return tableNum+getNumTables(timestamp, bestTime, people, idRestaurant, false, closeHit);
        }
    }
*/
    private void IncreaseTimeByThirtyMinutes(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.MINUTE, 30);
        timestamp.setTime(cal.getTime().getTime());
    }
    private void DecreaseTimeByThirtyMinutes(Timestamp timestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.MINUTE, -30);
        timestamp.setTime(cal.getTime().getTime());
    }

    public Integer getTablesForReservation(List<BigInteger> tableIds, Wrapper wrapper, Integer n,
            List<BigInteger> curTables, int guests){
        if(n == 0 || guests == 0)
            return 0;
        if(wrapper.getTablesCapacity().get(n-1) >= guests){
            if(guests - wrapper.getTablesCapacity().get(n-1) > wrapper.getMin()){
                wrapper.setMin(guests - wrapper.getTablesCapacity().get(n-1));
                List<BigInteger> newTableIds = new ArrayList<BigInteger>(curTables);
                newTableIds.add(tableIds.get(n-1));
                wrapper.setRet(newTableIds);
                System.out.println("TEST" + wrapper.getRet());
            }
            return getTablesForReservation(tableIds,wrapper, n-1, curTables, guests);
        } else {
            List<BigInteger> newTableIds = new ArrayList<BigInteger>(curTables);
            newTableIds.add(tableIds.get(n-1));

            return Math.max(wrapper.getTablesCapacity().get(n-1) + getTablesForReservation(tableIds, wrapper, n-1, newTableIds, guests-wrapper.getTablesCapacity().get(n-1)),
                    getTablesForReservation(tableIds,wrapper, n-1, curTables, guests));
        }

    }

    @Transactional
    public HashMap<BigInteger, Integer> getFreeTablesInCertainTime (Timestamp timestamp, int stayDuration, Long idRestaurant, int guests){
        String sql = "select t.table_id, t.capacity " +
                "from tables t " +
                "where t.restaurant_id = :idRestaurant " +
                "and not exists(select r.reservation_id " +
                "from reservations r " +
                "where t.table_id = r.table_id " +
                "and tsrange(r.time_from, r.time_to, '[]') && tsrange(:dateTime, :dateTimeEnd, '[]')) ";

        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);
        cal.add(Calendar.HOUR, stayDuration);
        Timestamp timestampEnd = new Timestamp(cal.getTime().getTime());

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("dateTime", timestamp);
        query.setParameter("idRestaurant", idRestaurant);
        query.setParameter("dateTimeEnd", timestampEnd);

        HashMap<BigInteger, Integer> tables = new LinkedHashMap<>();
        List<Object[]> tableObjects = query.getResultList();
        for(Object[] result : tableObjects){
            tables.put((BigInteger) result[0], (Integer) result[1]);
        }
        System.out.println("free tables" + tables);
        return tables;
    }
    public Boolean isSpaceAllowed(int guests, int space){
        return ceil(guests+guests*0.3) >= space && space >= guests;
    }
    public Boolean areGivenTablesFree(List<BigInteger> allFree, List<BigInteger> givenTables){
        return allFree.containsAll(givenTables);
    }
    @Transactional
    public Integer getDurationOfStay(Timestamp timestamp, Long idRestaurant, int guests){
        String sqlStay = "select rs.stay " +
                "from restaurant_stays rs " +
                "where rs.restaurant_id = :idRestaurant " +
                "and cast(rs.time_from as time) <= :dateTime " +
                "and cast(rs.time_to as time) >= :dateTime " +
                "and rs.guest = :guests " +
                "and :weekend = rs.weekend ";

        int stayDuration;
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp);

        boolean weekend = false;
        if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            weekend = true;
        }
        Query queryStay = entityManager.createNativeQuery(sqlStay);
        queryStay.setParameter("dateTime", new Date(timestamp.getTime()));
        queryStay.setParameter("idRestaurant", idRestaurant);
        queryStay.setParameter("guests", guests);
        queryStay.setParameter("weekend", weekend);


        List<Integer> stays = queryStay.getResultList();
        if(stays.isEmpty()) stayDuration = 1;
        else stayDuration = stays.get(0);

        return stayDuration;
    }
}

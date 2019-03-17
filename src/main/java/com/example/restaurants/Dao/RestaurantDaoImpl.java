package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hibernate.Criteria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Repository
public class RestaurantDaoImpl implements RestaurantDao {

    //inject the session factory
    private final EntityManager entityManager;


    public RestaurantDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<Restaurant> getRestuarant(JsonNode json) {
        //query uz pretpostavku da rezervacija traje 2h (time_from + 2h = time_to)
        //PARAMETRI: name, guests, date (ZA JSON!!!)
        /*String sql = "select r.name, r.priceRange, r.description " +
                "from Restaurant r " +
                "inner join r.tables t, r.city c " +
                "where (r.name like :name or c.city like :name) " +
                "and t.capacity >= :guests " +
                "and not exists ( select r1.time_from from Reservation r1 " +
                                "where r1.table.id = t.id " +
                                "and :date between r1.time_from and r1.time_to ) ";*/
        String sql = "select r " +
                "from Restaurant r, City c " +
                "where r.city = c and (r.name like :name or c.city like :name)";
        Query theQuery = entityManager.createQuery(sql, Restaurant.class);
        theQuery.setParameter("name", json.get("search").asText());
        List<Restaurant> restaurants = theQuery.getResultList();
        return restaurants;
    }

    @Override
    @Transactional
    public JsonNode getRandRestaurant() {

        //Query queryCount = entityManager.createQuery("select count(r) from Restaurant r");
        //Long size = (Long) queryCount.getSingleResult();
        //alternativa za bazu sa manje restorana : select * from restaurants r order by random() limit 6 (sporija varijanta u slucaju vece tabele, ali bolji randomize)

        //long[] index = new Random().longs(0, size, 6).toArray();

        Query theQuery = entityManager.createNativeQuery("select r.restaurant_id, r.avatar, r.name, r.price_range, string_agg(cou.name, ' | '), " +
                "(select avg(rev1.grade) "+
                "from reviews rev1 "+
                "where rev1.restaurant_id = r.restaurant_id), "+
                "(select count(rev1.grade) "+
                "from reviews rev1 "+
                "where rev1.restaurant_id = r.restaurant_id) "+
                "from restaurants r tablesample system_rows(6)" +
                "left join restaurant_cousine rc "+
                "on r.restaurant_id = rc.restaurant_id "+
                "left join cousines cou "+
                "on rc.cousine_id = cou.cousine_id " +
                "group by r.restaurant_id ");
        //theQuery.setParameter("indexs", index);
        System.out.println("-----------> " +theQuery.getResultList());
        List<Object []> restaurants = theQuery.getResultList();

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        restaurants.forEach(child -> {
            System.out.println();
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("id", (BigInteger) child[0]);
            ((ObjectNode) childNode).put("restaurantName", (String) child[2]);
            ((ObjectNode) childNode).put("priceRange", (String) child[3]);
            ((ObjectNode) childNode).put("imageFileName", (String) child[1]);
            ((ObjectNode) childNode).put("foodType", (String) child[4]);
            ((ObjectNode) childNode).put("mark", (Double) child[5]);
            ((ObjectNode) childNode).put("votes", (BigInteger) child[6]);
            array.add(childNode);
        });
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).putPOJO("restaurants", array);
        return res;
    }

    @Override
    @Transactional
    public JsonNode getSearchedRestaurants(JsonNode json) {
        int itemsPerPage = json.get("itemsPerPage").asInt();
        int pageNumber = (json.get("pageNumber").asInt()-1)*itemsPerPage;
        String name = json.get("searchText").asText();
        String priceFilter = json.get("filterPrice").asText();
        String ratingFilter = json.get("filterRating").asText();
        String cousineFilter = json.get("filterCousine").asText();

        String sqlCount = "select count(distinct r.restaurant_id)" +
                "from cities c,  restaurants r "+
                "left join restaurant_cousine rc "+
                "on r.restaurant_id = rc.restaurant_id "+
                "left join cousines cou "+
                "on rc.cousine_id = cou.cousine_id "+

                "where r.city_id = c.city_id and (r.name like :name or c.city like :name or "+
                "exists (select * from cousines cou1 where cou1.cousine_id = cou.cousine_id "+
                "and cou1.name like :name)) ";


        String sql = "select r.restaurant_id, r.name, r.price_range, r.avatar, "+
        "(select avg(rev1.grade) "+
        "from reviews rev1 "+
        "where rev1.restaurant_id = r.restaurant_id), "+
        "(select count(rev1.grade) "+
        "from reviews rev1 "+
        "where rev1.restaurant_id = r.restaurant_id), "+
        "string_agg(cou.name, ' | ') "+

        "from cities c,  restaurants r "+
        "left join restaurant_cousine rc "+
        "on r.restaurant_id = rc.restaurant_id "+
        "left join cousines cou "+
        "on rc.cousine_id = cou.cousine_id " +

        "where r.city_id = c.city_id ";

        if(!name.isEmpty())
            sql +=  "and (r.name like :name or c.city like :name or "+
                    "r.restaurant_id in (SELECT rc1.restaurant_id "+
                    "from restaurant_cousine rc1, cousines c1 "+
                    "where c1.cousine_id = rc1.cousine_id and c1.name like :name)) ";

        if(!priceFilter.isEmpty())
            sql+="and round(r.price_range) = :price ";


        if(!ratingFilter.isEmpty())
            sql+="and :rating = (select round(avg(rev1.grade)) " +
                    "from reviews rev1 " +
                    "where rev1.restaurant_id = r.restaurant_id) ";

        if(!cousineFilter.isEmpty())
            sql+="and 1 <= (select count(*) " +
                    "from restaurant_cousine rc1 " +
                    "where rc1.restaurant_id = r.restaurant_id " +
                    "and rc1.cousine_id in (1)) ";


        sql += "group by 1 order by 1 ";

        Query theQuery = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        if(!name.isEmpty())
            theQuery.setParameter("name", name+'%');
        if(!priceFilter.isEmpty())
            theQuery.setParameter("price", Double.parseDouble(priceFilter));
        if(!ratingFilter.isEmpty())
            theQuery.setParameter("rating", Double.parseDouble(ratingFilter));
        if(!cousineFilter.isEmpty()) {
            //theQuery.setParameter("cousine", cousineFilter);
           // theQuery.setParameter("cousineNum", StringUtils.countOccurrencesOf(cousineFilter, ",")+1);
        }
            queryCount.setParameter("name", name+'%');
        theQuery.setFirstResult(pageNumber);    //9*n (n€N0)
        theQuery.setMaxResults(itemsPerPage);   //9
        List<Object []> restaurants = theQuery.getResultList();
        BigInteger total = (BigInteger) queryCount.getSingleResult();
        int numberOfRestaurantPages = (int)Math.ceil(total.doubleValue()/itemsPerPage);

        System.out.println(total + "====" + numberOfRestaurantPages);
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        restaurants.forEach(child -> {
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("id", (BigInteger) child[0]);
            ((ObjectNode) childNode).put("restaurantName", (String) child[1]);
            ((ObjectNode) childNode).put("priceRange", (Double) child[2]);
            ((ObjectNode) childNode).put("imageFileName", (String) child[3]);
            ((ObjectNode) childNode).put("mark", (Double) child[4]);
            ((ObjectNode) childNode).put("votes", (BigInteger) child[5]);
            ((ObjectNode) childNode).put("foodType", (String) child[6]);
            array.add(childNode);
        });
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).putPOJO("restaurants", array);
        ((ObjectNode) res).put("numberOfRestaurantPages", numberOfRestaurantPages);
        ((ObjectNode) res).put("totalItems", total);

        return res;
    }

    @Override
    @Transactional
    public JsonNode getExtraDetails(Long id) {
        String sql = "select r from Restaurant r where r.id = :id";
        Query theQuery = entityManager.createQuery(sql, Restaurant.class);
        theQuery.setParameter("id", id);
        Restaurant r = (Restaurant) theQuery.getSingleResult();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).put("description", r.getDescription());
        return res;
    }
}

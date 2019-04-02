package com.example.restaurants.Dao;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Country;
import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.math.BigInteger;
import java.util.List;

@Repository
public class LocationDaoImpl implements LocationDao{

    private final EntityManager entityManager;


    public LocationDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public ArrayNode getPopularLocations() {
        Query theQuery = entityManager.createNativeQuery("select c.city, (select count(*) " +
                                                            "from restaurants r " +
                                                            "where c.city_id = r.city_id)" +
                                                            "from cities c");
        List<Object[]> objectList = theQuery.getResultList();
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();

        objectList.forEach(child -> {
            System.out.println();
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("city", (String) child[0]);
            ((ObjectNode) childNode).put("restaurantNum", (BigInteger) child[1]);
            array.add(childNode);
        });

        return array;
    }

    @Override
    public List<Country> getCountries() {
        Query theQuery = entityManager.createQuery("from Country",Country.class);
        return theQuery.getResultList();
    }

    @Override
    public List<City> getCities(JsonNode json) {
        System.out.println(json);
        System.out.println(json.get("countryId"));
        Query theQuery = entityManager.createQuery("from City c where c.country.id = :locationId",City.class);
        theQuery.setParameter("locationId", json.get("countryId").asLong());
        return theQuery.getResultList();
    }
    @Override
    public JsonNode getCitiesPagination(JsonNode json){
        int itemsPerPage = json.get("itemsPerPage").asInt();
        int pageNumber = (json.get("pageNumber").asInt()-1)*itemsPerPage;
        String name = json.get("searchText").asText();
        String sqlCount = "select count(distinct c.city_id) " +
                "from cities c "+
                "where c.city like :name ";


        String sql = "select c.city_id, c.city, cou.name "+
                "from cities c, countries cou "+
                "where c.city like :name " +
                "and c.country_id = cou.country_id";

        Query theQuery = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        theQuery.setParameter("name", name+'%');
        queryCount.setParameter("name", name+'%');
        theQuery.setFirstResult(pageNumber);    //9*n (n€N0)
        theQuery.setMaxResults(itemsPerPage);   //9
        List<Object []> locations = theQuery.getResultList();
        BigInteger total = (BigInteger) queryCount.getSingleResult();
        int numberOfRestaurantPages = (int)Math.ceil(total.doubleValue()/itemsPerPage);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        locations.forEach(child -> {
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("id", (BigInteger) child[0]);
            ((ObjectNode) childNode).put("city", (String) child[1]);
            ((ObjectNode) childNode).put("country", (String) child[2]);
            array.add(childNode);
        });
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).putPOJO("locations", array);
        ((ObjectNode) res).put("numberOfRestaurantPages", numberOfRestaurantPages);
        ((ObjectNode) res).put("totalItems", total);

        return res;
    }
}

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
}

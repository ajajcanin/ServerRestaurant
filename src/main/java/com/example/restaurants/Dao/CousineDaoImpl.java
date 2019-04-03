package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Cousine;
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
import java.util.List;

@Repository
public class CousineDaoImpl implements CousineDao {
    private final EntityManager entityManager;


    public CousineDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public List<Cousine> getAllCousines() {
        Query theQuery = entityManager.createQuery("from Cousine ", Cousine.class);
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public JsonNode getCousinesPagination(JsonNode json) {
        int itemsPerPage = json.get("itemsPerPage").asInt();
        int pageNumber = (json.get("pageNumber").asInt()-1)*itemsPerPage;
        String name = json.get("searchText").asText();
        String sqlCount = "select count(distinct c.cousine_id) " +
                "from cousines c "+
                "where c.name like :name ";


        String sql = "select c.cousine_id, c.name "+
                "from cousines c "+
                "where c.name like :name ";

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
            ((ObjectNode) childNode).put("name", (String) child[1]);
            array.add(childNode);
        });
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).putPOJO("cousines", array);
        ((ObjectNode) res).put("numberOfRestaurantPages", numberOfRestaurantPages);
        ((ObjectNode) res).put("totalItems", total);

        return res;
    }
}

package com.example.restaurants.Dao;

import com.example.restaurants.Entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        String sql = "from User r " +
                "where r.email like :email";
        Query theQuery = entityManager.createQuery(sql, User.class);
        theQuery.setParameter("email", email);
        try{
            return (User) theQuery.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean register(User user){
        if(this.findByEmail(user.getEmail()) != null)
            return false;
        entityManager.persist(user);
        return true;
    }

    @Override
    @Transactional
    public JsonNode getUsersPagination(JsonNode json){
        int itemsPerPage = json.get("itemsPerPage").asInt();
        int pageNumber = (json.get("pageNumber").asInt()-1)*itemsPerPage;
        String name = json.get("searchText").asText();
        String sqlCount = "select count(distinct u.user_id) " +
                "from users u "+
                "where u.first_name like :name " +
                "or u.last_name like :name ";


        String sql = "select u.user_id, u.first_name, u.last_name, u.email_address, u.phone_num, u.user_type, u.city_id  "+
                "from users u "+
                "where u.first_name like :name " +
                "or u.last_name like :name ";

        Query theQuery = entityManager.createNativeQuery(sql);
        Query queryCount = entityManager.createNativeQuery(sqlCount);

        theQuery.setParameter("name", name+'%');
        queryCount.setParameter("name", name+'%');
        theQuery.setFirstResult(pageNumber);    //9*n (n€N0)
        theQuery.setMaxResults(itemsPerPage);   //9
        List<Object []> users = theQuery.getResultList();
        BigInteger total = (BigInteger) queryCount.getSingleResult();
        int numberOfRestaurantPages = (int)Math.ceil(total.doubleValue()/itemsPerPage);

        ObjectMapper mapper = new ObjectMapper();
        ArrayNode array = mapper.createArrayNode();
        users.forEach(child -> {
            JsonNode childNode = mapper.createObjectNode();
            ((ObjectNode) childNode).put("id", (BigInteger) child[0]);
            ((ObjectNode) childNode).put("firstName", (String) child[1]);
            ((ObjectNode) childNode).put("lastName", (String) child[2]);
            ((ObjectNode) childNode).put("email", (String) child[3]);
            ((ObjectNode) childNode).put("phone", (String) child[4]);
            ((ObjectNode) childNode).put("type", (String) child[5]);
            ((ObjectNode) childNode).put("cityId", (BigInteger) child[6]);
            array.add(childNode);
        });
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).putPOJO("users", array);
        ((ObjectNode) res).put("numberOfRestaurantPages", numberOfRestaurantPages);
        ((ObjectNode) res).put("totalItems", total);

        return res;
    }
}

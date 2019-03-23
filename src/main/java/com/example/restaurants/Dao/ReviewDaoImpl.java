package com.example.restaurants.Dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.Query;

import javax.persistence.EntityManager;
import java.math.BigInteger;

@Repository
public class ReviewDaoImpl implements ReviewDao {
    private final EntityManager entityManager;


    public ReviewDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public JsonNode insertComment(JsonNode json) {
        String sqlId = "select u.user_id from users u where u.email_address LIKE :email";
        String sql = "update reviews SET grade = :mark, review = :comment " +
                "where restaurant_id = :idRestaurant and user_id = :idUser ; " +
                "insert into reviews(grade, review, restaurant_id, user_id) " +
                "values (:mark, :comment, :idRestaurant, :idUser) " +
                "where not exists (select * from reviews where restaurant_id = :idRestaurant and user_id = :idUser) ";
        System.out.println(json);
        Query queryUserId = entityManager.createNativeQuery(sqlId);
        queryUserId.setParameter("email", json.get("emailUser").asText());
        BigInteger id = (BigInteger) queryUserId.getSingleResult();

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("mark", json.get("mark").asDouble());
        query.setParameter("idRestaurant", json.get("idRestaurant").asLong());
        query.setParameter("idUser", id.longValue());
        query.setParameter("comment", json.get("comment").asText());
        query.executeUpdate();

        String avgSql = "select avg(grade) " +
                "from reviews " +
                "where restaurant_id = :idRestaurant ";
        Query avgQuery = entityManager.createNativeQuery(avgSql);
        String countSql = "select count(grade) " +
                "from reviews " +
                "where restaurant_id = :idRestaurant ";
        Query countQuery = entityManager.createNativeQuery(countSql);
        avgQuery.setParameter("idRestaurant", json.get("idRestaurant").asLong());
        countQuery.setParameter("idRestaurant", json.get("idRestaurant").asLong());

        double grade = (double) avgQuery.getSingleResult();
        BigInteger ratings = (BigInteger) countQuery.getSingleResult();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).put("grade", grade);
        ((ObjectNode) res).put("ratings", ratings);
        return res;
    }
}
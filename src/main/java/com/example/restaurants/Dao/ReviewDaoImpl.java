package com.example.restaurants.Dao;

import com.fasterxml.jackson.databind.JsonNode;
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
    public void insertComment(JsonNode json) {
        String sqlId = "select u.user_id from users u where u.email_address LIKE :email";
        String sql = "insert into reviews(grade, review, restaurant_id, user_id)" +
                        "values (:mark, :comment, :idRestaurant, :idUser)";
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
    }
}

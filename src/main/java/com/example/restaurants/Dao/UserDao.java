package com.example.restaurants.Dao;

import com.example.restaurants.Entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao{
    User findByEmail(String email);
    Boolean register(User user);
    JsonNode getUsersPagination(JsonNode data);
}

package com.example.restaurants.Dao;

import com.fasterxml.jackson.databind.JsonNode;

public interface ReviewDao {
    void insertComment(JsonNode json);
}

package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Cousine;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CousineDao {
    List<Cousine> getAllCousines();
    JsonNode getCousinesPagination(JsonNode json);
}

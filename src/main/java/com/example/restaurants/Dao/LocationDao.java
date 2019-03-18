package com.example.restaurants.Dao;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Country;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDao {
    ArrayNode getPopularLocations();

    List<Country> getCountries();
    List<City> getCities(JsonNode json);
}

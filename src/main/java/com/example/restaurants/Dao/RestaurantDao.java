package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RestaurantDao {
    public List<Restaurant> getRestuarant(JsonNode json);
    public JsonNode getRandRestaurant ();
    public JsonNode getSearchedRestaurants(JsonNode json);
    Restaurant getExtraDetails(Long id);
}

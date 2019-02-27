package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RestaurantDao {
    public List<Restaurant> getRestuarant(JsonNode json);
    public List<Restaurant> getRandRestaurant ();
}

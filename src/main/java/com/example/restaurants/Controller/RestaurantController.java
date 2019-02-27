package com.example.restaurants.Controller;

import com.example.restaurants.Dao.RestaurantDao;
import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class RestaurantController {

    private final RestaurantDao restaurantDAO;

    public RestaurantController( RestaurantDao restaurantDAO) {
        this.restaurantDAO = restaurantDAO;
    }
    @CrossOrigin
    @RequestMapping("/search")
    public List<Restaurant> search(@RequestBody String name) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(name);

        List<Restaurant> restaurants = restaurantDAO.getRestuarant(json);

        return restaurants;
    }
    @CrossOrigin
    @RequestMapping("/random")
    public List<Restaurant> randomRestaurants() throws IOException {

        List<Restaurant> restaurants = restaurantDAO.getRandRestaurant();

        return restaurants;
    }
}
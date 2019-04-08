package com.example.restaurants.Controller;

import com.example.restaurants.Dao.RestaurantDao;
import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
    public JsonNode randomRestaurants() throws IOException {

        JsonNode restaurants = restaurantDAO.getRandRestaurant();

        return restaurants;
    }
    @CrossOrigin
    @RequestMapping("/getRestaurantsByFilter")
    public JsonNode getSearchedRestaurants(@RequestBody String req) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req);

        JsonNode restaurants = restaurantDAO.getSearchedRestaurants(json);

        return restaurants;
    }
    @CrossOrigin
    @RequestMapping("/getExtraDetails")
    public Restaurant getExtraDetails(@RequestBody Long id) throws IOException{

        Restaurant restaurants = restaurantDAO.getExtraDetails(id);

        return restaurants;
    }
}
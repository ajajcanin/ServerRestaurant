package com.example.restaurants.Controller;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Cousine;
import com.example.restaurants.Entity.Restaurant;
import com.example.restaurants.Repository.AdminCousine;
import com.example.restaurants.Repository.AdminLocation;
import com.example.restaurants.Repository.AdminRestaurants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class AdminRestaurantController {
    private final AdminRestaurants adminRestaurants;
    private final AdminLocation adminLocation;
    private final AdminCousine adminCousine;

    public AdminRestaurantController(AdminRestaurants adminRestaurants, AdminLocation adminLocation, AdminCousine adminCousine) {
        this.adminRestaurants = adminRestaurants;
        this.adminLocation = adminLocation;
        this.adminCousine = adminCousine;
    }


    @CrossOrigin
    @RequestMapping("/addRestaurant")
    public ResponseEntity addRestaurant(@RequestBody String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data);
        JsonNode jsonCousines = json.get("cousines");

        String name = json.get("name").asText();
        double priceRange = json.get("priceRange").asDouble();
        String description = json.get("description").asText();
        String avatar = json.get("avatar").asText();
        String cover = json.get("cover").asText();
        double longitude = json.get("longitude").asDouble();
        double latitude = json.get("latitude").asDouble();
        long cityId = json.get("cityId").asLong();
        List<Cousine> cousines = new ArrayList<Cousine>();
        for (JsonNode node : jsonCousines){
            cousines.add(adminCousine.findCousineById(node.asLong()));
        }

        City city = new City();

        if(!adminLocation.existsById(cityId))
            return ResponseEntity.status(400).body("City does not exist");
        else{
            city = adminLocation.findCityById(cityId);
            Restaurant restaurant = new Restaurant(name, priceRange, description, avatar, cover, longitude, latitude, city, cousines);
            adminRestaurants.save(restaurant);
            return ResponseEntity.status(200).body(null);
        }
    }
    @CrossOrigin
    @RequestMapping("/editRestaurant")
    public ResponseEntity editRestaurant(@RequestBody String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data);
        JsonNode jsonCousines = json.get("cousines");

        Long id = json.get("id").asLong();
        String name = json.get("name").asText();
        double priceRange = json.get("priceRange").asDouble();
        String description = json.get("description").asText();
        String avatar = json.get("avatar").asText();
        String cover = json.get("cover").asText();
        double longitude = json.get("longitude").asDouble();
        double latitude = json.get("latitude").asDouble();
        long cityId = json.get("cityId").asLong();
        List<Cousine> cousines = new ArrayList<Cousine>();
        for (JsonNode node : jsonCousines){
            cousines.add(adminCousine.findCousineById(node.asLong()));
        }
        City city = new City();

        if(!adminLocation.existsById(cityId))
            return ResponseEntity.status(400).body("City does not exist");
        else if(!adminRestaurants.existsById(id))
            return ResponseEntity.status(400).body("City does not exist");
        else{
            city = adminLocation.findCityById(cityId);
            Restaurant restaurant = adminRestaurants.findRestaurantById(id);
            restaurant.setName(name);
            restaurant.setPriceRange(priceRange);
            restaurant.setDescription(description);
            restaurant.setAvatar(avatar);
            restaurant.setCover(cover);
            restaurant.setLongitude(longitude);
            restaurant.setLatitude(latitude);
            restaurant.setCity(city);
            restaurant.setCousines(cousines);
            adminRestaurants.save(restaurant);
            return ResponseEntity.status(200).body(null);
        }
    }
    @CrossOrigin
    @RequestMapping("/deleteRestaurant")
    public ResponseEntity deleteRestaurant(@RequestBody Long id) throws IOException {
        if(!adminRestaurants.existsById(id))
            return ResponseEntity.status(400).body("City does not exist");
        else{
            adminRestaurants.deleteById(id);
            return ResponseEntity.status(200).body(null);
        }
    }
}

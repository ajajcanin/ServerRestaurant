package com.example.restaurants.Controller;

import com.example.restaurants.Repository.AdminCousine;
import com.example.restaurants.Repository.AdminLocation;
import com.example.restaurants.Repository.AdminRestaurants;
import com.example.restaurants.Repository.AdminUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigInteger;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class AdminDashboardController {
    private AdminRestaurants adminRestaurant;
    private AdminLocation adminLocation;
    private AdminUser adminUser;

    public AdminDashboardController(AdminRestaurants adminRestaurant, AdminLocation adminLocation, AdminUser adminUser) {
        this.adminRestaurant = adminRestaurant;
        this.adminLocation = adminLocation;
        this.adminUser = adminUser;
    }
    @CrossOrigin
    @RequestMapping("/counters")
    public ResponseEntity deleteLocation() throws IOException {
        Long cRestaurant = adminRestaurant.count();
        Long cLocation = adminLocation.count();
        Long cUser = adminUser.count();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode res = mapper.createObjectNode();
        ((ObjectNode) res).put("restaurants", cRestaurant);
        ((ObjectNode) res).put("locations", cLocation);
        ((ObjectNode) res).put("users", cUser);
        return ResponseEntity.status(200).body(res);
    }
}

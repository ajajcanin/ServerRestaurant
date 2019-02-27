package com.example.restaurants.Controller;

import com.example.restaurants.Dao.LocationDao;
import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Country;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class LocationController {

    private final LocationDao locationDao;

    public LocationController( LocationDao locationDao) {
        this.locationDao = locationDao;
    }

    @CrossOrigin
    @RequestMapping("/popular-locations")
    public ArrayNode randomRestaurants() throws IOException {

        ArrayNode locations = locationDao.getPopularLocations();

        return locations;
    }

    @CrossOrigin
    @RequestMapping("/countries")
    public List<Country> countries() throws IOException {

        List<Country> countries = locationDao.getCountries();

        return countries;
    }
    @CrossOrigin
    @RequestMapping("/cities")
    public List<City> cities(@RequestBody String countryId) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(countryId);

        List<City> cities = locationDao.getCities(json);

        return cities;
    }
}

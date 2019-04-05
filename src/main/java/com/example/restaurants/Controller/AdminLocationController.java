package com.example.restaurants.Controller;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Country;
import com.example.restaurants.Repository.AdminCountry;
import com.example.restaurants.Repository.AdminLocation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class AdminLocationController {
    private final AdminLocation adminLocation;
    private final AdminCountry adminCountry;

    public AdminLocationController(AdminLocation adminLocation, AdminCountry adminCountry) {
        this.adminLocation = adminLocation;
        this.adminCountry = adminCountry;
    }

    @CrossOrigin
    @RequestMapping("/addLocation")
    public ResponseEntity addLocation(@RequestBody City city) throws IOException {
        if(adminLocation.existsById(city.getId()))
            return ResponseEntity.status(400).body(null);
        else{
            Country country = city.getCountry();
            if(!adminCountry.existsByName(country.getCountry()))
                adminCountry.save(country);
            adminLocation.save(city);
            return ResponseEntity.status(200).body(null);
        }
    }

    @CrossOrigin
    @RequestMapping("/editLocation")
    public ResponseEntity editLocation(@RequestBody City city) throws IOException {
        if(adminLocation.existsById(city.getId())){
            Country country = city.getCountry();
            if(!adminCountry.existsByName(country.getCountry()))
                city.setCountry(adminCountry.save(country));
            country.setId(adminCountry.findCountryByName(country.getCountry()).getId());
            City oldCity = adminLocation.findCityById(city.getId());
            oldCity.setCity(city.getCity());
            oldCity.setCountry(city.getCountry());
            adminLocation.save(oldCity);
            return ResponseEntity.status(200).body(null);
        }
        else
            return ResponseEntity.status(400).body("Location does not exist");
    }
    @CrossOrigin
    @RequestMapping("/deleteLocation")
    public ResponseEntity deleteLocation(@RequestBody Long id) throws IOException {
        if(adminLocation.existsById(id)){
            adminLocation.deleteById(id);
            return ResponseEntity.status(200).body(null);
        }
        else
            return ResponseEntity.status(400).body("Location does not exist");
    }
}

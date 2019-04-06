package com.example.restaurants.Controller;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Restaurant;
import com.example.restaurants.Entity.User;
import com.example.restaurants.Repository.AdminLocation;
import com.example.restaurants.Repository.AdminUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class AdminUserController {

    private final AdminUser adminUser;
    private final AdminLocation adminLocation;

    public AdminUserController(AdminUser adminUser, AdminLocation adminLocation) {
        this.adminUser = adminUser;
        this.adminLocation = adminLocation;
    }

    @CrossOrigin
    @RequestMapping("/addUser")
    public ResponseEntity addUser(@RequestBody String data) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data);

        String firstName = json.get("firstName").asText();
        String lastName = json.get("lastName").asText();
        String email = json.get("email").asText();
        String pass = json.get("pass").asText();
        String phone = json.get("phone").asText();
        String userType = json.get("userType").asText();
        long cityId = json.get("cityId").asLong();

        if(!adminLocation.existsById(cityId))
            return ResponseEntity.status(400).body("City does not exist");
        else{
            City city = adminLocation.findCityById(cityId);
            User user = new User(firstName, lastName, email, pass, phone, userType);
            user.setCity(city);
            adminUser.save(user);
            return ResponseEntity.status(200).body(null);
        }
    }

    @CrossOrigin
    @RequestMapping("/editUser")
    public ResponseEntity editUser(@RequestBody String data) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data);

        Long id = json.get("id").asLong();
        String firstName = json.get("firstName").asText();
        String lastName = json.get("lastName").asText();
        String email = json.get("email").asText();
        String pass = json.get("pass").asText();
        String phone = json.get("phone").asText();
        String userType = json.get("userType").asText();
        long cityId = json.get("cityId").asLong();

        if(!adminLocation.existsById(cityId))
            return ResponseEntity.status(400).body("City does not exist");
        else if(!adminUser.existsById(id))
            return ResponseEntity.status(400).body("User does not exist");
        else{
            User user = adminUser.findUserById(id);
            String oldPass = user.getPassword();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            if(user.getPassword().isEmpty()) user.setPassword(pass);
            user.setPhoneNum(phone);
            user.setUserType(userType);
            City city = adminLocation.findCityById(cityId);
            user.setCity(city);
            adminUser.save(user);
            return ResponseEntity.status(200).body(null);
        }
    }

    @CrossOrigin
    @RequestMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestBody Long id) throws IOException {
        if(adminUser.existsById(id)){
            adminUser.deleteById(id);
            return ResponseEntity.status(200).body(null);
        }
        else
            return ResponseEntity.status(400).body("Location does not exist");
    }

    @CrossOrigin
    @RequestMapping("/isAdmin")
    public boolean isAdmin(@RequestBody String email) throws IOException{
        User x = adminUser.findByEmail(email);
        System.out.println(x.getUserType());
        return x.getUserType().equals("admin");
    }
}

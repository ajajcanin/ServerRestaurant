package com.example.restaurants.Controller;

import com.example.restaurants.Dao.UserDao;
import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class UserController {

    private final UserDao userDao;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    public UserController(UserDao userDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @RequestMapping("/register")
    public ResponseEntity<Object> register(@RequestBody String req) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req);
        User user = new User(json.get("firstName").textValue(), json.get("lastName").textValue(),
                json.get("email").textValue(), json.get("password").textValue(), json.get("phoneNum").textValue(), "user");
        JsonNode jsonCity = json.get("city");
        City city = new City(jsonCity.get("id").longValue(), json.get("city").textValue());
        user.setCity(city);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        if(!userDao.register(user))
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        return null;
    }

    @RequestMapping("/usersPagination")
    public JsonNode getUsersPagination(@RequestBody String data) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(data);

        JsonNode users = userDao.getUsersPagination(json);

        return users;
    }

}

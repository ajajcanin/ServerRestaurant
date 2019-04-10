package com.example.restaurants.Controller;

import com.example.restaurants.Entity.Cousine;
import com.example.restaurants.Repository.AdminCousine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class AdminCousineController {
    private final AdminCousine adminCousine;

    public AdminCousineController(AdminCousine adminCousine) {
        this.adminCousine = adminCousine;
    }
    @CrossOrigin
    @RequestMapping("/addCategory")
    public ResponseEntity addCousine(@RequestBody Cousine cousine) throws IOException {
        if(adminCousine.existsByName(cousine.getName()))
            return ResponseEntity.status(400).body(null);
        adminCousine.save(cousine);
        return ResponseEntity.status(200).body(null);
    }

    @CrossOrigin
    @RequestMapping("/editCategory")
    public ResponseEntity editCousine(@RequestBody String cousine) throws IOException {
        if(!adminCousine.existsByName(cousine)){
            Cousine oldCousine = adminCousine.findCousineByName(cousine);
            oldCousine.setName(cousine);
            adminCousine.save(oldCousine);
            return ResponseEntity.status(200).body(null);
        }
        else
            return ResponseEntity.status(400).body("Category does not exist");
    }
    @CrossOrigin
    @RequestMapping("/deleteCategory")
    public ResponseEntity deleteCousine(@RequestBody Long id) throws IOException {
        if(adminCousine.existsById(id)){
            adminCousine.deleteById(id);
            return ResponseEntity.status(200).body(null);
        }
        else
            return ResponseEntity.status(400).body("Category does not exist");
    }
}

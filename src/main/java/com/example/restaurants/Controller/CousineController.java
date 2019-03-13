package com.example.restaurants.Controller;

import com.example.restaurants.Dao.CousineDao;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping(value = "/app")
public class CousineController {

    private final CousineDao cousineDao;

    public CousineController(CousineDao cousineDao) {
        this.cousineDao = cousineDao;
    }

    @CrossOrigin
    @RequestMapping("/cousines")
    public ResponseEntity getCousines() throws IOException {
        return cousineDao.getAllCousines();
    }
}

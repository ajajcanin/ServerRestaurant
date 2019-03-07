package com.example.restaurants.Controller;

import com.example.restaurants.Dao.ReservationDao;
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
public class ReservationController {

    private final ReservationDao reservationDao;

    public ReservationController(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    @CrossOrigin
    @RequestMapping("/checkReservationAvailability")
    public ResponseEntity checkReservationAvailability(@RequestBody String req) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req);
        return reservationDao.checkReservationAvailability(json);
    }
    @CrossOrigin
    @RequestMapping("/makeReservation")
    public ResponseEntity makeReservation(@RequestBody String req) throws IOException {
        System.out.println("BLAAAAAAAA->>>>>>>>>>>" + req);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(req);
        return reservationDao.makeReservation(json);
    }
}

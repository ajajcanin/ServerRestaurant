package com.example.restaurants.Dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;

public interface ReservationDao {
    ResponseEntity checkReservationAvailability(JsonNode json);
    ResponseEntity makeReservation(JsonNode json);
}

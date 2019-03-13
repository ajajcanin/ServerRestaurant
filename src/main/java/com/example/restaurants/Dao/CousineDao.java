package com.example.restaurants.Dao;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface CousineDao {
    ResponseEntity getAllCousines();
}

package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Cousine;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CousineDao {
    List<Cousine> getAllCousines();
}

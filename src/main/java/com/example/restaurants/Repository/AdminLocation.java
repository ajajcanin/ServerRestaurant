package com.example.restaurants.Repository;

import com.example.restaurants.Entity.City;
import com.example.restaurants.Entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface AdminLocation extends CrudRepository<City, Long> {
    City findCityById(Long id);
}

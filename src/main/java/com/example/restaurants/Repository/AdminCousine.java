package com.example.restaurants.Repository;

import com.example.restaurants.Entity.Cousine;
import com.example.restaurants.Entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface AdminCousine extends CrudRepository<Cousine, Long> {
    Cousine findCousineById(Long id);
    boolean existsByName(String name);
}

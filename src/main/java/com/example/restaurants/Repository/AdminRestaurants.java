package com.example.restaurants.Repository;

import com.example.restaurants.Entity.Restaurant;
import org.springframework.data.repository.CrudRepository;

public interface AdminRestaurants extends CrudRepository<Restaurant, Long> {
    Restaurant findRestaurantById (Long id);
}

package com.example.restaurants.Repository;

import com.example.restaurants.Entity.Country;
import org.springframework.data.repository.CrudRepository;

public interface AdminCountry extends CrudRepository<Country,Long> {
    boolean existsByName(String name);
    Country findCountryByName(String name);
}

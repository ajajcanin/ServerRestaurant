package com.example.restaurants.Repository;

import com.example.restaurants.Entity.Cousine;
import com.example.restaurants.Entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface AdminUser extends CrudRepository<User, Long> {
    User findUserById(Long id);
    User findByEmail(String email);
}

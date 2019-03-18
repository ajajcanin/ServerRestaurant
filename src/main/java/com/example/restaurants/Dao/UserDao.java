package com.example.restaurants.Dao;

import com.example.restaurants.Entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao{
    User findByEmail(String email);
    Boolean register(User user);
}

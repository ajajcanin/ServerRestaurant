package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Cousine;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Repository
public class CousineDaoImpl implements CousineDao {
    private final EntityManager entityManager;


    public CousineDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    @Transactional
    public List<Cousine> getAllCousines() {
        Query theQuery = entityManager.createQuery("select c from Cousine c", Cousine.class);
        return theQuery.getResultList();
    }
}

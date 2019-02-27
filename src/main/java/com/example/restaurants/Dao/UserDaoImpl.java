package com.example.restaurants.Dao;

import com.example.restaurants.Entity.User;
import com.example.restaurants.WebSecurity;
import com.sun.deploy.security.UserDeclinedException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

@Repository
public class UserDaoImpl implements UserDao {

    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public User findByEmail(String email) {
        String sql = "from User r " +
                "where r.email like :email";
        Query theQuery = entityManager.createQuery(sql, User.class);
        theQuery.setParameter("email", email);
        try{
            return (User) theQuery.getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    @Override
    @Transactional
    public Boolean register(User user){
        if(this.findByEmail(user.getEmail()) != null)
            return false;
        entityManager.persist(user);
        return true;
    }


}

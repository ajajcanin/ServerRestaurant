package com.example.restaurants.Dao;

import com.example.restaurants.Entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Random;

@Repository
public class RestaurantDaoImpl implements RestaurantDao {

    //inject the session factory
    private final EntityManager entityManager;


    public RestaurantDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public List<Restaurant> getRestuarant(JsonNode json) {
        //query uz pretpostavku da rezervacija traje 2h (time_from + 2h = time_to)
        //PARAMETRI: name, guests, date (ZA JSON!!!)
        /*String sql = "select r.name, r.priceRange, r.description " +
                "from Restaurant r " +
                "inner join r.tables t, r.city c " +
                "where (r.name like :name or c.city like :name) " +
                "and t.capacity >= :guests " +
                "and not exists ( select r1.time_from from Reservation r1 " +
                                "where r1.table.id = t.id " +
                                "and :date between r1.time_from and r1.time_to ) ";*/
        String sql = "from Restaurant r inner join r.city c " +
                "where (r.name like :name or c.city like:name)";
        TypedQuery<Restaurant> theQuery = entityManager.createQuery(sql, Restaurant.class);
        List<Restaurant> restaurants = theQuery.getResultList();
        return restaurants;
    }

    @Override
    @Transactional
    public List<Restaurant> getRandRestaurant() {

        //Query queryCount = entityManager.createQuery("select count(r) from Restaurant r");
        //Long size = (Long) queryCount.getSingleResult();
        //alternativa za bazu sa manje restorana : select * from restaurants r order by random() limit 6 (sporija varijanta u slucaju vece tabele, ali bolji randomize)

        //long[] index = new Random().longs(0, size, 6).toArray();

        Query theQuery = entityManager.createNativeQuery("select r.restaurant_id, r.avatar, r.cover, r.description, r.name, r.price_range, r.city_id from restaurants r tablesample system_rows(6)", Restaurant.class);
        //theQuery.setParameter("indexs", index);
        System.out.println(theQuery.getResultList());
        List<Restaurant> restaurants= theQuery.getResultList();
        return restaurants;
    }
}

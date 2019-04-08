package com.example.restaurants.Entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name="restaurant_stays")
public class RestaurantStays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="stay_id", nullable=false)
    private long id;

    @Column(name="start", nullable=false)
    private Timestamp start;

    @Column(name="end", nullable=false)
    private Timestamp end;

    @Column(name="guest", nullable=false)
    private int guest;

    @Column(name="weekend", nullable=false)
    private boolean weekend;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;


    public RestaurantStays(Timestamp start, Timestamp end, int guest, boolean weekend, Restaurant restaurant) {
        this.start = start;
        this.end = end;
        this.guest = guest;
        this.weekend = weekend;
        this.restaurant = restaurant;
    }

    public RestaurantStays() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getStart() {
        return start;
    }

    public void setStart(Timestamp start) {
        this.start = start;
    }

    public Timestamp getEnd() {
        return end;
    }

    public void setEnd(Timestamp end) {
        this.end = end;
    }

    public int getGuest() {
        return guest;
    }

    public void setGuest(int guest) {
        this.guest = guest;
    }

    public boolean isWeekend() {
        return weekend;
    }

    public void setWeekend(boolean weekend) {
        this.weekend = weekend;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

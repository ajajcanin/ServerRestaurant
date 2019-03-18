package com.example.restaurants.Entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Table(name="reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="reservation_id", nullable=false)
    private long id;

    @Column(name="guests", nullable=false)
    private int guests;

    @Column(name="time_from", nullable=false)
    private Timestamp time_from;

    @Column(name="time_to", nullable=false)
    private Timestamp time_to;

    @ManyToOne
    @JoinColumn(name="table_id")
    private com.example.restaurants.Entity.Table table;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Reservation(){}

    public Reservation(int guests, Timestamp time_from, Timestamp time_to) {
        this.guests = guests;
        this.time_from = time_from;
        this.time_to = time_to;
    }

    public long getId() {
        return id;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Timestamp getTime_from() {
        return time_from;
    }

    public void setTime_from(Timestamp time_from) {
        this.time_from = time_from;
    }

    public Timestamp getTime_to() {
        return time_to;
    }

    public void setTime_to(Timestamp time_to) {
        this.time_to = time_to;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", guests=" + guests +
                ", time_from=" + time_from +
                ", time_to=" + time_to +
                '}';
    }
}

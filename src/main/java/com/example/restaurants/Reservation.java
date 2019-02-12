package com.example.restaurants;

import javax.persistence.*;
import javax.persistence.Table;
import java.sql.Time;

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
    private Time time_from;

    @Column(name="time_to", nullable=false)
    private Time time_to;

    @ManyToOne
    @JoinColumn(name="table_id")
    private com.example.restaurants.Table table;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    public Reservation(int guests, Time time_from, Time time_to) {
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

    public Time getTime_from() {
        return time_from;
    }

    public void setTime_from(Time time_from) {
        this.time_from = time_from;
    }

    public Time getTime_to() {
        return time_to;
    }

    public void setTime_to(Time time_to) {
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

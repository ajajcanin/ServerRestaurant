package com.example.restaurants.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@javax.persistence.Table(name="tables")
public class Table {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="table_id", nullable=false)
    private long id;

    @Column(name="capacity", nullable=false)
    private int capacity;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "table")
    private List<Reservation> reservations;

    public Table(){}

    public Table(int capacity) {
        this.capacity = capacity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", capacity=" + capacity +
                '}';
    }
}

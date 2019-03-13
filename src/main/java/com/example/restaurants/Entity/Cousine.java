package com.example.restaurants.Entity;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name="cousines")
public class Cousine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="cousine_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false)
    private String name;


    public Cousine(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Cousine(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

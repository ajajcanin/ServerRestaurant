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


}

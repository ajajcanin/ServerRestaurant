package com.example.restaurants;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false, unique=true)
    private String name;

    @ManyToMany
    @JoinTable(
            name="restaurant_category",
            joinColumns = @JoinColumn(name="category_id"),
            inverseJoinColumns = @JoinColumn(name="restaurant_id")
    )
    private List<Restaurant> restaurants;

    public Category(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Categories{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

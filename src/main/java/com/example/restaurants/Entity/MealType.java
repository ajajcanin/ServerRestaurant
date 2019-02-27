package com.example.restaurants.Entity;

import javax.persistence.Table;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="meal_types")
public class MealType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy = "mealType")
    private List<Meal> meals;

    public MealType(){}

    public MealType(String name) {
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

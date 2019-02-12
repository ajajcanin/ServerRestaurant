package com.example.restaurants;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name="meals")
public class Meal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="meal_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="price", nullable=false)
    private String price;

    @Column(name="type", nullable=false)
    private String type;

    @Column(name="description")
    private String description;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name="meal_type_id")
    private MealType mealType;

    public Meal(String name, String price, String type, String description) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return type;
    }

    public void setCategory(String category) {
        this.type = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", category='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

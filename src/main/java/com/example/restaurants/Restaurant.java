package com.example.restaurants;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="restaurants")
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="restaurant_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false)
    private String name;

    @Column(name="price_range", nullable=false)
    private String priceRange;

    @Column(name="description")
    private String description;

    @ManyToMany
    @JoinTable(
            name="restaurant_category",
            joinColumns = @JoinColumn(name="restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="category_id")
    )
    private List<Category> categories;

    @OneToMany(mappedBy = "restaurant")
    private List<Meal> meals;

    @OneToMany(mappedBy = "restaurant")
    private List<com.example.restaurants.Table> tables;

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    public Restaurant(String name, String priceRange, String description) {
        this.name = name;
        this.priceRange = priceRange;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

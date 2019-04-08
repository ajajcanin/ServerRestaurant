package com.example.restaurants.Entity;

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
    private double priceRange;

    @Column(name="description", length = 750)
    private String description;

    @Column(name="avatar")
    private String avatar;

    @Column(name="cover")
    private String cover;

    @Column(name="longitude")
    private double longitude;

    @Column(name="latitude")
    private double latitude;

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
    private List<com.example.restaurants.Entity.Table> tables;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    @OneToMany(mappedBy = "restaurant")
    private List<Review> reviews;

    @OneToMany(mappedBy = "restaurant")
    private List<RestaurantStay> restaurantStays;

    @ManyToOne
    @JoinColumn(name="city_id")
    private City city;

    @ManyToMany
    @JoinTable(
            name="restaurant_cousine",
            joinColumns = @JoinColumn(name="restaurant_id"),
            inverseJoinColumns = @JoinColumn(name="cousine_id")
    )
    private List<Cousine> cousines;

    public Restaurant(){}
    public Restaurant(String name, double priceRange, String description) {
        this.name = name;
        this.priceRange = priceRange;
        this.description = description;
    }

    public Restaurant(String name, double priceRange, String description, String avatar, String cover, double longitude, double latitude, City city, List<Cousine> cousines) {
        this.name = name;
        this.priceRange = priceRange;
        this.description = description;
        this.avatar = avatar;
        this.cover = cover;
        this.longitude = longitude;
        this.latitude = latitude;
        this.city = city;
        this.cousines = cousines;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(double priceRange) {
        this.priceRange = priceRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public List<Cousine> getCousines() {
        return cousines;
    }

    public void setCousines(List<Cousine> cousines) {
        this.cousines = cousines;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priceRange='" + priceRange + '\'' +
                ", description='" + description + '\'' +
                ", avatar='" + avatar + '\'' +
                ", cover='" + cover + '\'' +
                ", categories=" + categories +
                ", meals=" + meals +
                ", tables=" + tables +
                ", reviews=" + reviews +
                ", city=" + city +
                ", cousines=" + cousines +
                '}';
    }
}

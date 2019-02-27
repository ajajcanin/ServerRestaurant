package com.example.restaurants.Entity;


import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="countries")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="country_id", nullable=false)
    private long id;

    @Column(name="name", nullable=false, unique=true)
    private String name;

    @OneToMany(mappedBy = "country")
    private List<City> cities;

    public Country(){}

    public Country(String country) {
        this.name = country;
    }

    public String getCountry() {
        return name;
    }

    public void setCountry(String country) {
        this.name = country;
    }

    public long getId() {
        return id;
    }
    @Override
    public String toString() {
        return "Countries{" +
                "country='" + name + '\'' +
                '}';
    }

}

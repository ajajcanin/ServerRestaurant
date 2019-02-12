package com.example.restaurants;


import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="country")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="country_id", nullable=false)
    private long id;

    @Column(name="country", nullable=false, unique=true)
    private String country;

    @OneToMany(mappedBy = "country")
    private List<City> cities;

    public Country(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "Countries{" +
                "country='" + country + '\'' +
                '}';
    }

}

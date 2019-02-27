package com.example.restaurants.Entity;

import javax.persistence.*;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="city_id", nullable=false)
    private long id;

    @Column(name="city", nullable=false)
    private String city;

    @OneToMany(mappedBy = "city")
    private List<Restaurant> restaurants;

    @OneToMany(mappedBy = "city")
    private List<User> users;

    @ManyToOne
    @JoinColumn(name="country_id")
    private Country country;

    /*@Column(name="postal_code", nullable=false)
    private String postalCode;*/

    public City(){}
    public City(Long id, String city) {
        this.id = id;
        this.city = city;
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    /*public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }*/

    @Override
    public String toString() {
        return "Cities{" +
                "id=" + id +
                ", city='" + city + '\'' +
                //", postalCode='" + postalCode + '\'' +
                '}';
    }
}

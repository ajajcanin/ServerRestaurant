package com.example.restaurants;

import javax.persistence.*;
import javax.persistence.Table;

@Entity
@Table(name="reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id", nullable=false)
    private long id;

    @Column(name="review")
    private String review;

    @Column(name="grade")
    private String grade;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="restaurant_id")
    private Restaurant restaurant;

    public Review(String review, String grade) {
        this.review = review;
        this.grade = grade;
    }

    public long getId() {
        return id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Reviews{" +
                "id=" + id +
                ", review='" + review + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}

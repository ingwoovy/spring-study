package com.study.springstudy.domain;


import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "user_orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private Integer price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    protected Order () {}
    public Order (Long id, String productName, Integer price,User user) {
        this.id = id;
        this. productName = productName;
        this.price = price;
        this.user = user;
    }

    public Long getId() {
        return id;
    }
    public String getProductName() {
        return productName;
    }
    public Integer getPrice() {
        return price;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

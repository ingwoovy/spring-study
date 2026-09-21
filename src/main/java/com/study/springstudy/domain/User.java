package com.study.springstudy.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<Order> orders = new ArrayList<>();


    protected User () {}
    public User (Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
       return name;
    }
    public int getAge() {
        return age;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void update(String name, Integer age) {
        if (name != null) {
            this.name = name;
        }

        if (age != null) {
            this.age = age;
        }
    }

    public void addOrder (Order order) {
        orders.add(order);
        order.setUser(this);
    }

    public void removeOrder (Order order) {
        orders.remove(order);
        order.setUser(null);
    }
}
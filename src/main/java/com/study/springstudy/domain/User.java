package com.study.springstudy.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
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

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

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

    public User(String name,Integer age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
        this.role = Role.ROLE_USER;
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
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public Role getRole() {
        return role;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
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
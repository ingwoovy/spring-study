package com.study.springstudy.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int age;

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

    public void update(String name, Integer age) {
        if (name != null) {
            this.name = name;
        }

        if (age != null) {
            this.age = age;
        }
    }
}
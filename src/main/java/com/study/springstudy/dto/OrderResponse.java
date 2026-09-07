package com.study.springstudy.dto;

import com.study.springstudy.domain.User;

public class OrderResponse {

    private Long id;
    private String productName;
    private Integer price;
    private Long userId;
    private String userName;

    public OrderResponse(Long id,
                         String productName,
                         Integer price,
                         Long userId,
                         String userName) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
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
    public Long getUserId() {
        return userId;
    }
    public String getUserName() { return  userName;}
}

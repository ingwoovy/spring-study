package com.study.springstudy.dto;

public class OrderCreateRequest {

    private Long userId;
    private String productName;
    private Integer price;

    public OrderCreateRequest() {}

    public OrderCreateRequest(Long userId, String productName, Integer price) {
        this.userId = userId;
        this.productName = productName;
        this.price = price;
    }

    public Long getUserId() {
        return userId;
    }
    public String getProductName() {
        return productName;
    }
    public Integer getPrice() {
        return price;
    }
}

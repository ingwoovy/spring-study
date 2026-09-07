package com.study.springstudy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @GetMapping("/products")
    public String searchProducts(
            @RequestParam String category,
            @RequestParam int maxPrice
    ) {
        return "category = " + category + ", maxPrice = " + maxPrice;
    }

}

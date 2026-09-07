package com.study.springstudy.exception;

public class OrderNotFoundException extends RuntimeException{

    public OrderNotFoundException () {
        super("Order Not Found");
    }
}

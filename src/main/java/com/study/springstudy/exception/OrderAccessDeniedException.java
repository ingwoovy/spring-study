package com.study.springstudy.exception;

public class OrderAccessDeniedException extends RuntimeException {

    public OrderAccessDeniedException () {
        super("해당 사용자의 주문이 아닙니다.");
    }
}

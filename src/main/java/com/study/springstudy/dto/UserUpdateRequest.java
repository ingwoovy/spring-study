package com.study.springstudy.dto;

import jakarta.validation.constraints.Min;

public class UserUpdateRequest {

    private String name;
    @Min(value = 0, message = "나이는 0이상이어야합니다.")
    private Integer age;

    public UserUpdateRequest () {}

    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
}

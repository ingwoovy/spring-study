package com.study.springstudy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public class UserCreateRequest {

    @NotBlank(message = "이름은 필수입니다.")
    private String name;

    @NotNull(message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야합니다.")
    private Integer age;

    public UserCreateRequest() {
    }

    public UserCreateRequest (String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
}

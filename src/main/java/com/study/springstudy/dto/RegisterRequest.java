package com.study.springstudy.dto;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank (message = "이름은 필수입니다.")
    private String name;

    @NotNull (message = "나이는 필수입니다.")
    @Min(value = 0, message = "나이는 0 이상이어야합니다.")
    private Integer age;

    @NotBlank (message = "이메일은 필수입니다.")
    @Email (message = "올바른 이메일 형식이어야합니다.")
    private String email;

    @NotBlank (message = "비밀번호는 필수입니다.")
    @Size (min = 8, message = "비밀번호는 최소 8자 이상이어야합니다.")
    private String password;

    public RegisterRequest() {}

    public RegisterRequest(String name, Integer age, String email, String password) {
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public Integer getAge() {
        return age;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
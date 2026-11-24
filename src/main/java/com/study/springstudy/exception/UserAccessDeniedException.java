package com.study.springstudy.exception;

public class UserAccessDeniedException extends RuntimeException {

    public UserAccessDeniedException() {
        super("해당 사용자에 대한 접근 권한이 없습니다.");
    }
}
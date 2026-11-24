package com.study.springstudy.global.security;

import com.study.springstudy.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SecurityErrorResponseWriter {

    private static final ObjectMapper objectMapper =
            new ObjectMapper();

    public static void write(
            HttpServletResponse response,
            int status,
            String message
    ) throws IOException {

        ErrorResponse errorResponse =
                new ErrorResponse(status, message, null);

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                objectMapper.writeValueAsString(errorResponse)
        );
    }
}
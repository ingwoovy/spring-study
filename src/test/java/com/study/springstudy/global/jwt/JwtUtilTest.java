package com.study.springstudy.global.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

import static io.jsonwebtoken.Jwts.SIG.HS256;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void generateTokenSuccess() {

        // given
        String email = "hong@example.com";

        // when
        String token = jwtUtil.generateToken(email);

        // then
        assertNotNull(token);
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);
    }

    @Test
    void getEmailFromTokenSuccess() {

        // given
        String email = "hong@example.com";
        String token = jwtUtil.generateToken(email);

        // when
        String extractedEmail = jwtUtil.getEmailFromToken(token);

        // then
        assertEquals(email, extractedEmail);
    }

    @Test
    void tamperedTokenRejected() {

        // given
        String tokenA = jwtUtil.generateToken("hong@example.com");
        String tokenB = jwtUtil.generateToken("admin@example.com");

        String[] partsA = tokenA.split("\\.");
        String[] partsB = tokenB.split("\\.");

        String tamperedToken = partsA[0] + "." + partsB[1] + "." + partsA[2];

        // when & then
        assertThrows(
                SignatureException.class,
                () -> jwtUtil.getEmailFromToken(tamperedToken)
        );
    }

    @Test
    void expiredTokenRejected() {

        // given
        SecretKey testKey = Jwts.SIG.HS256.key().build();

        Date now = new Date();
        Date issuedDate = new Date(now.getTime() - 3600000);
        Date expiredDate = new Date(now.getTime() - 1000);

        String expiredToken = Jwts.builder()
                .subject("hong@example.com")
                .issuedAt(issuedDate)
                .expiration(expiredDate)
                .signWith(testKey,HS256)
                .compact();

        // when & then
        assertThrows(
                ExpiredJwtException.class,
                () -> Jwts.parser()
                        .verifyWith(testKey)
                        .build()
                        .parseSignedClaims(expiredToken)
        );
    }
}
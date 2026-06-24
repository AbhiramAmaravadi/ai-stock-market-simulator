package com.abhiram.stocktrader.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET = "mySuperSecretKeyForStockTraderProject123456";

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis()
                                + 1000 * 60 * 60 * 24))
                .signWith(
                        SignatureAlgorithm.HS256,
                        SECRET)
                .compact();
    }
}
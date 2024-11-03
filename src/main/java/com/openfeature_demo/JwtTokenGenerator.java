package com.openfeature_demo;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JwtTokenGenerator {

    private final String secretKey;

    public JwtTokenGenerator(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateToken(String username) {
        long expirationTime = 365L * 24 * 60 * 60 * 1000; // 1 year in milliseconds
        Date issuedAt = new Date();
        Date expirationDate = new Date(issuedAt.getTime() + expirationTime);

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "EMPLOYEE");

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, this.secretKey) // Changed to HS256
                .compact();
    }


    private static String loadSecretKeyFromProperties() {
        Properties properties = new Properties();
        try (InputStream input = JwtTokenGenerator.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find jwt.properties");
            }
            properties.load(input);
            return properties.getProperty("security.jwt.secret-key");
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load secret key from properties file", ex);
        }
    }

    public static void main(String[] args) {
        String email;

        if (args.length > 0) {
            email = args[0];
        } else {
            System.out.print("Enter email address: ");
            Scanner scanner = new Scanner(System.in);
            email = scanner.nextLine();
            scanner.close();
        }

        String secretKey = loadSecretKeyFromProperties();
        JwtTokenGenerator jwtTokenGenerator = new JwtTokenGenerator(secretKey);

        String token = jwtTokenGenerator.generateToken(email);
        System.out.println("Generated JWT Token: " + token);
    }
}

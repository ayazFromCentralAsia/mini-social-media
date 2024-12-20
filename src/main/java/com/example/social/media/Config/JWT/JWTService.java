package com.example.social.media.Config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    public String generateToken(String email) {
        return JWT.create()
                .withClaim("email", email)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(ZonedDateTime.now().plusWeeks(3).toInstant()))
                .sign(Algorithm.HMAC512(SECRET_KEY));
    }

    public String validateToken(String token) {
        JWTVerifier jwtVerifier = JWT
                .require(Algorithm.HMAC512(SECRET_KEY))
                .build();
        DecodedJWT jwt = jwtVerifier.verify(token);
        return jwt.getClaim("email").asString();
    }

}

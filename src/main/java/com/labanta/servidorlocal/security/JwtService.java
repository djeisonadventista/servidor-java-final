package com.labanta.servidorlocal.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service public class JwtService { @Value("${jwt.secret}") private String secret; private SecretKey getChaveSecreta() { byte[] keyBytes = Decoders.BASE64.decode(secret); return Keys.hmacShaKeyFor(keyBytes); }

    public String gerarToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //1 hora
                .signWith(getChaveSecreta())
                .compact();
    }

    public String extrairUsername(String token) {

        return Jwts.parser()
                .verifyWith(getChaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
package com.example.mailbackend.util;


import com.example.mailbackend.security.UserPrincipal;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {

    private static final long EXPIRE_DURATION = 60 * 60 *  1000;

    private final String SECRET_KEY = "mailbackend";

    public String generateAccessToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .setSubject(String.format("%s,%s", userPrincipal.getId(), userPrincipal.getUsername()))
                .setIssuer("mailbackend")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateAccessToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) {
            throw  ex;
        } catch (IllegalArgumentException ex) {

        } catch (MalformedJwtException ex) {

        } catch (UnsupportedJwtException ex) {

        } catch (SignatureException ex) {

        }
        return false;
    }

    public String getSubject(String accessToken) {
        return parseClaims(accessToken).getSubject();
    }

    public Claims parseClaims(String accessToken) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(accessToken)
                .getBody();
    }
}

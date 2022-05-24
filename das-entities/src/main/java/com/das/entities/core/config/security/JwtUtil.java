package com.das.entities.core.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil implements Serializable {
    static final String CLAIM_KEY_USERNAME = "sub";
    static final String CLAIM_KEY_CREATED = "iat";
    private static final long serialVersionUID = -3301605591108950415L;

    @Autowired
    SecurityConfig securityConfig;

    public String getUsernameFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims == null ? null : claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public JwtUser getUserDetails(String token) {
        if (token == null) {
            return null;
        }

        try {
            final Claims claims = getClaimsFromToken(token);

            if (claims == null) {
                return null;
            }

            List<SimpleGrantedAuthority> authorities = null;
//            if (claims.get(CLAIM_KEY_AUTHORITIES) != null) {
//                authorities = ((List<String>) claims.get(CLAIM_KEY_AUTHORITIES)).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//            }

            return new JwtUser(claims.getSubject(), authorities);
        } catch (Exception e) {
            return null;
        }
    }

    public Date getExpirationDateFromToken(String token) {
        try {
            final Claims claims = getClaimsFromToken(token);
            return claims == null ? null : claims.getExpiration();
        } catch (Exception e) {
            return null;
        }
    }

    private Claims getClaimsFromToken(String token) {
        try {
            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }

            return Jwts.parser()
                    .setSigningKey(this.securityConfig.getSecret())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + this.securityConfig.getExpiration() * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(JwtUser userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getLoginDto().getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS256, this.securityConfig.getSecret())
                .compact();
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        JwtUser user = (JwtUser) userDetails;
        final String username = getUsernameFromToken(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token));
    }

    public String refreshToken(String token) {
        String refreshedToken;

        try {
            final Claims claims = getClaimsFromToken(token);
            if (claims == null)
                refreshedToken = null;
            else {
                claims.put(CLAIM_KEY_CREATED, new Date());
                refreshedToken = generateToken(claims);
            }
        } catch (Exception e) {
            refreshedToken = null;
        }

        return refreshedToken;
    }

}

package com.unindra.ngrancang.jwt;

import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.services.UserService;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {
    @Value("${env.data.jwtSecret}")
    private String jwtSecret;

    @Value("${env.data.jwtExpirationMs}")
    private int jwtExpirationMs;

	@Autowired
    UserService userService;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateJwtToken(Authentication authentication) {

		User userPrincipal = (User) authentication.getPrincipal();
		return Jwts.builder()
                .setId(userPrincipal.getId().toString())
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .claim("user", userPrincipal)
				.compact();
	}

	public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
			return true;
		} catch (JwtException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		}

		return false;
	}
    
    public String generateRefreshToken(Authentication authentication) {
		User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setId(userPrincipal.getId().toString())
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs * 7))
                .signWith(getSigningKey())
                .claim("user", userPrincipal)
				.compact();
    }
}

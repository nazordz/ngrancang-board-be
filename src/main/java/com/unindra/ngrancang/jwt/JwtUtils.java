package com.unindra.ngrancang.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.unindra.ngrancang.dto.responses.RoleResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.services.UserService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.modelmapper.TypeToken;

@Component
public class JwtUtils {
    @Value("${env.data.jwtSecret}")
    private String jwtSecret;

    @Value("${env.data.jwtExpirationMs}")
    private int jwtExpirationMs;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
    UserService userService;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateJwtToken(Authentication authentication) {

		User userPrincipal = (User) authentication.getPrincipal();
		UserResponse userResponse = modelMapper.map(userPrincipal, UserResponse.class);

		userResponse.setRoles(modelMapper.map(userPrincipal.getRoles(), new TypeToken<List<RoleResponse>>() {}.getType()));
		// userResponse.setRoles(userPrincipal.getRoles().stream().map(role -> modelMapper.map(role, RoleResponse.class)).toList());
		
		return Jwts.builder()
                .setId(userPrincipal.getId().toString())
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .claim("user", userResponse)
				.compact();
	}

	public String getUserNameFromJwtToken(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody().getSubject();
	}
	public User getUserFromJwtToken(String jwt) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(jwt).getBody().get("user", User.class);
	}

	public boolean validateJwtToken(String authToken) {
		Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
		return true;
	}
    
    public String generateRefreshToken(Authentication authentication) {
		User userPrincipal = (User) authentication.getPrincipal();
		UserResponse userResponse = modelMapper.map(userPrincipal, UserResponse.class);
		userResponse.setRoles(modelMapper.map(userPrincipal.getRoles(), new TypeToken<List<RoleResponse>>() {}.getType()));

        return Jwts.builder()
                .setId(userPrincipal.getId().toString())
				.setSubject((userPrincipal.getUsername()))
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs * 7))
                .signWith(getSigningKey())
                .claim("user", userResponse)
				.compact();
    }
}

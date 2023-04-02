package com.unindra.ngrancang.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.ngrancang.dto.LoginRequest;
import com.unindra.ngrancang.dto.LoginResponse;
import com.unindra.ngrancang.dto.MessageResponse;
import com.unindra.ngrancang.dto.RegisterRequest;
import com.unindra.ngrancang.jwt.JwtUtils;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.model.UserRole;
import com.unindra.ngrancang.repository.RoleRepository;
import com.unindra.ngrancang.repository.UserRepository;
import com.unindra.ngrancang.repository.UserRoleRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/auth")
@Slf4j
public class AuthController {
    @Autowired
	PasswordEncoder encoder;

    @Value("${env.data.jwtExpirationMs}")
    private int jwtExpirationMs;

	@Autowired
	JwtUtils jwtUtils;

    @Autowired
	AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRoleRepository userRoleRepository;

    @PostMapping("/signin")
    public ResponseEntity<?> login(
        @Valid @RequestBody LoginRequest request
    ) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())  
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);
            String refresh = jwtUtils.generateRefreshToken(authentication);
            
            LoginResponse res = new LoginResponse(
                jwt, refresh, "Bearer", jwtExpirationMs
            );
            return ResponseEntity.ok(res);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }


    }

    @PostMapping("/signup")
    public ResponseEntity<?> register(
        @Valid @RequestBody RegisterRequest registerRequest
    ) {
        try {
            if (userRepository.existsByEmail(registerRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            }
            User user = new User();
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            user.setName(registerRequest.getName());
            user.setPosition(registerRequest.getPosition());
            user.setPassword(encoder.encode(registerRequest.getPassword()));
            user.setIsActive(false);
            User storedUser = userRepository.save(user);

            UserRole userRole = new UserRole();
            userRole.setRole(roleRepository.findByName("ROLE_USER").get());
            userRole.setUser(user);
            userRoleRepository.save(userRole);
            
            return ResponseEntity.ok().body(storedUser);
        } catch (Exception e) {
            return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: " + e.getMessage()));
        }
        
    }
}

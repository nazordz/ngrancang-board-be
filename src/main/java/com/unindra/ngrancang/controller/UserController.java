package com.unindra.ngrancang.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.ngrancang.dto.RegisterRequest;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/user")
@Slf4j
public class UserController {
    
    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired UserService service;

    @PostMapping("/save")
    public User saveUser( @RequestBody RegisterRequest userRequestDto ) {
        User user = new User();
        user.setName(userRequestDto.getName());
        user.setEmail(userRequestDto.getEmail());
        user.setPhone(userRequestDto.getPhone());
        user.setPosition(userRequestDto.getPosition());
        user.setPassword(userRequestDto.getPassword());
        return userService.saveUser(user);
    }

    @GetMapping("/detail")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> detailUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("currentUser : {}", currentUser);
        return ResponseEntity.ok(currentUser);
    }


    @GetMapping
    public ResponseEntity<List<User>> findAllWithRoles() {
        return ResponseEntity.ok(service.findAll());
    }
}

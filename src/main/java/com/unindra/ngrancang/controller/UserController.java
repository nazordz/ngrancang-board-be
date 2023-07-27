package com.unindra.ngrancang.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unindra.ngrancang.dto.requests.RegisterRequest;
import com.unindra.ngrancang.dto.responses.RoleResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/users")
@Slf4j
public class UserController {
    
    @Autowired
    private ModelMapper modelMapper;
    
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
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> detailUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.debug("currentUser : {}", currentUser);
        User user = service.findById(currentUser.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> findAllWithRoles() {
        List<User> users = service.findAll();
        List<UserResponse> userResponses = users.stream().map(u -> {
            UserResponse usr = modelMapper.map(u, UserResponse.class);
            usr.setRoles(modelMapper.map(u.getRoles(), new TypeToken<List<RoleResponse>>() {}.getType()));
            return usr;
        }).toList();

        return ResponseEntity.ok(userResponses);
    }
}

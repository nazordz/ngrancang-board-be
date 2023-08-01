package com.unindra.ngrancang.controller;

import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.unindra.ngrancang.dto.requests.RegisterRequest;
import com.unindra.ngrancang.dto.requests.UpdateUserProfileRequest;
import com.unindra.ngrancang.dto.requests.UpdateUserStatusRequest;
import com.unindra.ngrancang.dto.responses.RoleResponse;
import com.unindra.ngrancang.dto.responses.UserResponse;
import com.unindra.ngrancang.jwt.UserDetailsServiceImpl;
import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.repository.UserRepository;
import com.unindra.ngrancang.services.UserService;

@RestController
@RequestMapping(path = "/api/users")
public class UserController {
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserDetailsServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired UserService service;

    @Autowired
	PasswordEncoder encoder;
    
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
    public ResponseEntity<UserResponse> detailUser() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.findById(currentUser.getId());
        UserResponse response = modelMapper.map(user, UserResponse.class);
        return ResponseEntity.ok(response);
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

    @GetMapping("/paginate")
    public Page<UserResponse> userPagination(
        @RequestParam(defaultValue = "0", name = "page") int page,
        @RequestParam(defaultValue = "10", name = "size") int size,
        @RequestParam(defaultValue = "", name = "search") String search
    ) {
        Pageable pageable = PageRequest.of(page, size);
        if (search.isBlank()) {
            Page<User> paginateData = userRepository.findAll(pageable);
            return paginateData.map(mapper -> modelMapper.map(mapper, UserResponse.class));
        }
        Page<User> paginateData = userRepository.findByEmailContainingIgnoreCaseOrNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(search, search, search, pageable);
        return paginateData.map(mapper -> modelMapper.map(mapper, UserResponse.class));
    }

    @PatchMapping("/{user_id}/status")
    public ResponseEntity<UserResponse> updateStatus(
        @PathVariable("user_id") UUID userId,
        @RequestBody UpdateUserStatusRequest request
    ) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));
        user.setIsActive(request.getIsActive());
        User updatedUser = userRepository.save(user);
        UserResponse response = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(response);
    } 


    @PutMapping
    public ResponseEntity<UserResponse> updateLoggedUser(
        @RequestBody UpdateUserProfileRequest request
    ) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = service.findById(currentUser.getId());
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setPosition(request.getPosition());
        if (request.isChangePassword()) {
            user.setPassword(encoder.encode(request.getNewPassword()));
        }
        User updatedUser = userRepository.save(user);
        UserResponse response = modelMapper.map(updatedUser, UserResponse.class);
        return ResponseEntity.ok(response);
    }
}

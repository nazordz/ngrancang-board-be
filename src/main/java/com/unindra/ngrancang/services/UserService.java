package com.unindra.ngrancang.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unindra.ngrancang.model.User;
import com.unindra.ngrancang.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class UserService {


    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public List<User> findAll() {
        List<User> users = userRepository.findAllWithRoles();
        
        return users;
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Error: User is not found."));
    }

    public User findById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Error: User is not found."));
    }

}

package com.example.springcrud.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcrud.entities.User;
import com.example.springcrud.repositories.IUserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public User findById(Integer id) {

        return userRepository.findById(id).orElseThrow();

    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow();

    }

}

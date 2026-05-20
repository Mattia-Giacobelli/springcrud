package com.example.springcrud.service;

import org.springframework.stereotype.Service;

import com.example.springcrud.repositories.IUserRepository;

@Service
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {

        this.userRepository = userRepository;

    }

}

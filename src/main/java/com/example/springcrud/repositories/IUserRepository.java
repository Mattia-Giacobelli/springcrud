package com.example.springcrud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcrud.entities.User;

public interface IUserRepository extends JpaRepository<User, Integer> {

}

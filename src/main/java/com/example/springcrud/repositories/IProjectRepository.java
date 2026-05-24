package com.example.springcrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcrud.entities.Project;

public interface IProjectRepository extends JpaRepository<Project, Integer> {

    Integer countByEmployeesId(Integer id);

}

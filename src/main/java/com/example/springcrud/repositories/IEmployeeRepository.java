package com.example.springcrud.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcrud.entities.Employee;
import java.time.LocalDate;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByUserEmailContainingIgnoreCase(String email);

    List<Employee> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate);

}

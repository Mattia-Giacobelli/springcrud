package com.example.springcrud.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.springcrud.entities.Employee;
import com.example.springcrud.utilities.Role;

import java.time.LocalDate;

public interface IEmployeeRepository extends JpaRepository<Employee, Integer> {

    Page<Employee> findByUserEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<Employee> findByBirthDateBetween(LocalDate minDate, LocalDate maxDate, Pageable pageable);

    Optional<Employee> findByUserUsername(String username);

    Page<Employee> findBySalaryGreaterThanEqual(Integer salary, Pageable pageable);

    Page<Employee> findByRole(Role role, Pageable pageable);

    @Query("select e from Employee e JOIN e.projects p where p.id = ?1")
    List<Employee> findByAssignedProjectId(Integer id);

    @Query("SELECT e FROM Employee e LEFT JOIN e.projects p WHERE p.id != ?1 OR p.id IS NULL")
    List<Employee> findByUnassignedProjectId(Integer id);
}

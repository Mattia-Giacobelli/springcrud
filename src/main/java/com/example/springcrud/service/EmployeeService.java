package com.example.springcrud.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springcrud.entities.Employee;
import com.example.springcrud.entities.Permission;
import com.example.springcrud.repositories.IEmployeeRepository;
import com.example.springcrud.repositories.IPermissionRepository;

@Service
public class EmployeeService {

    private final IEmployeeRepository employeeRepo;
    private final IPermissionRepository permissionRepo;

    public EmployeeService(IEmployeeRepository employeeRepo, IPermissionRepository permissionRepo) {

        this.employeeRepo = employeeRepo;
        this.permissionRepo = permissionRepo;

    }

    public List<Employee> index() {

        return employeeRepo.findAll();

    }

    public List<Employee> findByEmail(String email) {

        return employeeRepo.findByUserEmailContainingIgnoreCase(email);

    }

    public Employee findById(Integer id) {

        return employeeRepo.findById(id).orElseThrow();

    }

    public List<Employee> findByAge(Integer age) {

        LocalDate minDate = LocalDate.now().minusYears(age + 1).plusDays(1);
        LocalDate maxDate = LocalDate.now().minusYears(age);

        return employeeRepo.findByBirthDateBetween(minDate, maxDate);

    }

    public Employee create(Employee emp) {

        return employeeRepo.save(emp);

    }

    public Employee update(Employee emp) {

        return employeeRepo.save(emp);

    }

    public void delete(Integer id) {

        employeeRepo.deleteById(id);

    }

}

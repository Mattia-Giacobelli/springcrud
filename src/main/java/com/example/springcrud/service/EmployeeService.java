package com.example.springcrud.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcrud.entities.Employee;
import com.example.springcrud.entities.Permission;
import com.example.springcrud.entities.Project;
import com.example.springcrud.repositories.IEmployeeRepository;
import com.example.springcrud.repositories.IPermissionRepository;
import com.example.springcrud.repositories.IProjectRepository;
import com.example.springcrud.utilities.Role;

import jakarta.validation.constraints.Email;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private final IEmployeeRepository employeeRepo;
    private final IPermissionRepository permissionRepo;
    private final IProjectRepository projectRepo;

    public EmployeeService(IEmployeeRepository employeeRepo, IPermissionRepository permissionRepo,
            IProjectRepository projectRepo) {

        this.employeeRepo = employeeRepo;
        this.permissionRepo = permissionRepo;
        this.projectRepo = projectRepo;

    }

    public Page<Employee> index(String email, Integer age, Role role, Double salary, Pageable pageable) {

        // seleziona tutto
        Specification<Employee> spec = (root, query, cb) -> cb.conjunction();

        if (email != null && !email.trim().isEmpty()) {

            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("user").get("email")),
                    "%" + email.toLowerCase() + "%"));

        }

        if (age != null) {

            LocalDate minDate = LocalDate.now().minusYears(age + 1).plusDays(1);
            LocalDate maxDate = LocalDate.now().minusYears(age);

            spec = spec.and((root, query, cb) -> cb.between(root.get("birthDate"), minDate, maxDate));

        }

        if (role != null) {

            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), role));

        }

        if (salary != null) {

            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("salary"), salary));

        }

        return employeeRepo.findAll(spec, pageable);

    }

    public Page<Employee> findByEmail(String email, Pageable pageable) {

        return employeeRepo.findByUserEmailContainingIgnoreCase(email, pageable);

    }

    public Employee findById(Integer id) {

        return employeeRepo.findById(id).orElseThrow();

    }

    public Page<Employee> findByAge(Integer age, Pageable pageable) {

        LocalDate minDate = LocalDate.now().minusYears(age + 1).plusDays(1);
        LocalDate maxDate = LocalDate.now().minusYears(age);

        return employeeRepo.findByBirthDateBetween(minDate, maxDate, pageable);

    }

    public Page<Employee> findBySalary(Integer salary, Pageable pageable) {

        return employeeRepo.findBySalaryGreaterThanEqual(salary, pageable);

    }

    public Page<Employee> findByRole(Role role, Pageable pageable) {

        return employeeRepo.findByRole(role, pageable);

    }

    public List<Employee> findByAssignedProjectS(Integer projectId) {

        List<Employee> employees = employeeRepo.findAll();

        Project project = projectRepo.findById(projectId).orElseThrow();

        List<Employee> assignedEmps = employees.stream()
                .filter(emp -> emp.getProjects().contains(project)).toList();

        return assignedEmps;

    }

    public List<Employee> findByUnassignedProjectS(Integer projectId) {

        List<Employee> employees = employeeRepo.findAll();

        Project project = projectRepo.findById(projectId).orElseThrow();

        List<Employee> unassignedEmps = employees.stream()
                .filter(emp -> !emp.getProjects().contains(project)).toList();

        return unassignedEmps;

    }

    public List<Employee> findByAssignedProjectQ(Integer projectId) {

        List<Employee> assignedEmps = employeeRepo.findByAssignedProjectId(projectId);

        return assignedEmps;

    }

    public List<Employee> findByUnassignedProjectQ(Integer projectId) {

        List<Employee> unassignedEmps = employeeRepo.findByUnassignedProjectId(projectId);

        return unassignedEmps;

    }

    public Employee findByUsername(String username) {

        return employeeRepo.findByUserUsername(username).orElseThrow();

    }

    public Integer countByEmployeesId(Integer id) {

        return projectRepo.countByEmployeesId(id);

    }

    @Transactional
    public Employee create(Employee emp) {

        return employeeRepo.save(emp);

    }

    @Transactional(rollbackFor = Exception.class)
    public Employee update(Employee emp) {

        return employeeRepo.save(emp);

    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Integer id) {

        employeeRepo.deleteById(id);

    }

}

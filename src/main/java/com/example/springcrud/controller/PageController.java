package com.example.springcrud.controller;

import org.springframework.stereotype.Controller;

import com.example.springcrud.service.EmployeeService;
import com.example.springcrud.service.PermissionService;
import com.example.springcrud.utilities.AgeUtility;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    private final EmployeeService employeeService;

    private final PermissionService permissionService;

    private final AgeUtility ageUtility;

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=(?:.*[A-Z]){2})(?=.*\\d)(?=(?:.*[#!&-]){2})[a-zA-Z\\d#!&-]{6,10}$";

    public PageController(EmployeeService employeeService, AgeUtility ageUtility, PermissionService permissionService) {

        this.employeeService = employeeService;
        this.permissionService = permissionService;
        this.ageUtility = ageUtility;

    }

    @GetMapping("/")
    public String root() {

        return "redirect:/login";

    }

    @GetMapping("/home")
    public String home() {

        return "pages/home";

    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "/access-denied";
    }

}

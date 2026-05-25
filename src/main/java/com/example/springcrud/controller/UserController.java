package com.example.springcrud.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcrud.entities.Employee;
import com.example.springcrud.entities.User;
import com.example.springcrud.service.EmployeeService;
import com.example.springcrud.service.PermissionService;
import com.example.springcrud.service.UserService;
import com.example.springcrud.utilities.AgeUtility;
import com.example.springcrud.utilities.PermissionType;
import com.example.springcrud.utilities.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    private final BCryptPasswordEncoder passwordEncoder;

    private final UserService userService;

    private final EmployeeService employeeService;

    private final PermissionService permissionService;

    private final AgeUtility ageUtility;

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=(?:.*[A-Z]){2})(?=.*\\d)(?=(?:.*[#!&-]){2})[a-zA-Z\\d#!&-]{6,10}$";

    public UserController(UserService userService, PermissionService permissionService, EmployeeService employeeService,
            AgeUtility ageUtility, BCryptPasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.permissionService = permissionService;
        this.employeeService = employeeService;
        this.ageUtility = ageUtility;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("/profile")
    public String userProfile(@AuthenticationPrincipal UserDetails userD, Model userModel) {

        System.out.println("PROFILE");
        System.out.println(userD.getUsername());

        User user = userService.findByUsername(userD.getUsername());

        userModel.addAttribute("user", user);

        return "/pages/user/user";
    }

    @GetMapping("/update")
    public String updateUser(@AuthenticationPrincipal UserDetails userD, Model userM) {

        userM.addAttribute("employee", employeeService.findByUsername(userD.getUsername()));
        userM.addAttribute("roles", Role.values());
        userM.addAttribute("permissions", permissionService.findAll());

        return "pages/user/userForm";

    }

    @PutMapping("/update")
    public String putMethodName(
            @Validated @ModelAttribute("employee") Employee emp, BindingResult result,
            @AuthenticationPrincipal User user, RedirectAttributes red, Model empModel,
            HttpServletRequest request) {

        int age = ageUtility.getAge(emp.getBirthDate());
        String pass = emp.getUser().getPassword();

        boolean hasErrors = result.getFieldErrors().stream()
                .anyMatch(error -> !error.getField().equals("user.password"));

        if (hasErrors || age < 18 || (!pass.matches(PASSWORD_REGEX) && pass != "")) {

            empModel.addAttribute("employee", emp);
            empModel.addAttribute("age", age);
            empModel.addAttribute("pass", false);

            return "pages/user/userForm";

        } else {

            Employee oldEmp = employeeService.findByUsername(user.getUsername());

            if (pass == "") {

                emp.getUser().setPassword(oldEmp.getUser().getPassword());

            } else {

                emp.getUser().setPassword(passwordEncoder.encode(emp.getUser().getPassword()));

            }

            emp.setId(oldEmp.getId());

            if (user.getPermission().getPermissionType() != PermissionType.ADMIN) {

                emp.setRole(oldEmp.getRole());
                emp.setSalary(oldEmp.getSalary());
                emp.setHiringDate(oldEmp.getHiringDate());
                emp.getUser().setPermission(oldEmp.getUser().getPermission());

            }

            if (oldEmp.equals(emp)) {

                red.addFlashAttribute("msg", "Nessuna modifica apportata");

                return "redirect:/user/profile";

            }

            User newUser = emp.getUser();

            if (newUser.getUsername().equals(user.getUsername())
                    && !newUser.getPermission().equals(user.getPermission())) {

                employeeService.update(emp);

                try {

                    request.logout();

                } catch (ServletException e) {

                    e.printStackTrace();

                }

            }

            employeeService.update(emp);

            red.addFlashAttribute("msg", "Dipendente modificato correttamente");

            return "redirect:/user/profile";

        }

    }

}

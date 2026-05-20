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

    @GetMapping("/update/{id}")
    public String getMethodName(@PathVariable Integer id, Model userM) {

        userM.addAttribute("employee", employeeService.findById(id));

        return "pages/user/userForm";

    }

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable Integer id, @Validated @ModelAttribute("employee") Employee emp,
            BindingResult result, RedirectAttributes red, Model empModel) {

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

            Employee oldEmp = employeeService.findById(emp.getId());

            if (pass == "") {

                emp.getUser().setPassword(oldEmp.getUser().getPassword());

            } else {

                emp.getUser().setPassword(passwordEncoder.encode(emp.getUser().getPassword()));

            }

            if (oldEmp.equals(emp)) {

                red.addFlashAttribute("msg", "Nessuna modifica apportata");

                return "redirect:/user/profile";

            }

            employeeService.update(emp);

            red.addFlashAttribute("msg", "Dipendente modificato correttamente");

            return "redirect:/user/profile";

        }

    }

}

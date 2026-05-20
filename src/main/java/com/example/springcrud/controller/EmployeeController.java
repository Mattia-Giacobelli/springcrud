package com.example.springcrud.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.springcrud.entities.Employee;
import com.example.springcrud.service.EmployeeService;
import com.example.springcrud.service.PermissionService;
import com.example.springcrud.utilities.AgeUtility;
import com.example.springcrud.utilities.Role;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final BCryptPasswordEncoder passwordEncoder;

    private final EmployeeService employeeService;

    private final PermissionService permissionService;

    private final AgeUtility ageUtility;

    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=(?:.*[A-Z]){2})(?=.*\\d)(?=(?:.*[#!&-]){2})[a-zA-Z\\d#!&-]{6,10}$";

    public EmployeeController(EmployeeService employeeService, PermissionService permissionService,
            AgeUtility ageUtility, BCryptPasswordEncoder passwordEncoder) {

        this.employeeService = employeeService;
        this.permissionService = permissionService;
        this.ageUtility = ageUtility;
        this.passwordEncoder = passwordEncoder;

    }

    @GetMapping("")
    public String empIndex(@RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "age", required = false) Integer age,
            Model empModel) {

        if (email != null && !email.trim().isEmpty()) {

            empModel.addAttribute("employees", employeeService.findByEmail(email));

        } else if (age != null) {

            empModel.addAttribute("employees", employeeService.findByAge(age));

        } else {

            empModel.addAttribute("employees", employeeService.index());

        }

        return "pages/employees/index";

    }

    @GetMapping("/create")
    public String empCreateForm(Model empModel) {

        Employee newEmp = new Employee();

        empModel.addAttribute("employee", newEmp);
        empModel.addAttribute("roles", Role.values());
        empModel.addAttribute("permissions", permissionService.findAll());

        // System.out.println(java.util.Arrays.toString(Role.values()));

        return "pages/employees/empForm";

    }

    @PostMapping("/create")
    public String createEmp(@Validated @ModelAttribute("employee") Employee emp, BindingResult result,

            Model empModel, RedirectAttributes red) {

        // System.out.println(emp);

        int age = ageUtility.getAge(emp.getBirthDate());
        String pass = emp.getUser().getPassword();

        if (result.hasErrors() || age < 18 || !pass.matches(PASSWORD_REGEX)) {

            System.out.println("errore");

            empModel.addAttribute("roles", Role.values());
            empModel.addAttribute("age", age);
            empModel.addAttribute("permissions", permissionService.findAll());
            empModel.addAttribute("pass", false);

            return "pages/employees/empForm";

        } else {

            emp.getUser().setPassword(passwordEncoder.encode(emp.getUser().getPassword()));

            Employee empNew = employeeService.create(emp);

            red.addFlashAttribute("msg", empNew.getFiscalCode() + ", Dipendente aggiunto correttamente");

            return "redirect:/employees";

        }

    }

    @GetMapping("/update/{id}")
    public String getMethodName(@PathVariable Integer id, Model empModel) {

        empModel.addAttribute("employee", employeeService.findById(id));
        empModel.addAttribute("roles", Role.values());
        empModel.addAttribute("permissions", permissionService.findAll());

        Employee emp = employeeService.findById(id);
        // System.out.println("birthDate: " + emp.getBirthDate());

        return "pages/employees/empForm";

    }

    @PutMapping("/{id}")
    public String putMethodName(@PathVariable Integer id, @Validated @ModelAttribute("employee") Employee emp,
            BindingResult result, RedirectAttributes red, Model empModel) {

        int age = ageUtility.getAge(emp.getBirthDate());
        String pass = emp.getUser().getPassword();

        if (result.hasErrors() || age < 18 || !pass.matches(PASSWORD_REGEX)) {

            empModel.addAttribute("employee", emp);
            empModel.addAttribute("roles", Role.values());
            empModel.addAttribute("permissions", permissionService.findAll());
            empModel.addAttribute("age", age);
            empModel.addAttribute("pass", false);

            return "pages/employees/empForm";

        } else {

            emp.getUser().setPassword(passwordEncoder.encode(emp.getUser().getPassword()));

            Employee oldEmp = employeeService.findById(emp.getId());

            if (oldEmp.equals(emp)) {

                red.addFlashAttribute("msg", emp.getFiscalCode() + ", Nessuna modifica apportata");

                return "redirect:/employees";

            }

            employeeService.update(emp);

            red.addFlashAttribute("msg", emp.getFiscalCode() + ", Dipendente modificato correttamente");

            return "redirect:/employees";

        }

    }

    @DeleteMapping("/")
    public String deletePerson(@RequestParam("id") Integer id, RedirectAttributes red) {

        Employee emp = employeeService.findById(id);

        employeeService.delete(id);

        red.addFlashAttribute("msg", emp.getFiscalCode() + ", Dipendente eliminato correttamente");

        return "redirect:/employees";

    }

}

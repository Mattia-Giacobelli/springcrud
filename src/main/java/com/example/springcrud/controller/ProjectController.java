package com.example.springcrud.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import com.example.springcrud.entities.Project;
import com.example.springcrud.service.EmployeeService;
import com.example.springcrud.service.ProjectService;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final EmployeeService empService;

    public ProjectController(ProjectService projectService, EmployeeService empService) {

        this.projectService = projectService;
        this.empService = empService;

    }

    @GetMapping("")
    public String Index(Model projectM, @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, 5, Sort.by("name").ascending());
        Page<Project> projects = projectService.findAll(pageable);

        projectM.addAttribute("projects", projects);

        return "pages/projects/index";

    }

    @GetMapping("/create")
    public String CreateForm(Model projectM) {

        Project newProject = new Project();

        projectM.addAttribute("project", newProject);

        // System.out.println(java.util.Arrays.toString(Role.values()));

        return "pages/projects/projectForm";

    }

    @PostMapping("/create")
    public String createProject(@Validated @ModelAttribute("project") Project project,
            BindingResult result, RedirectAttributes red) {

        // System.out.println(emp);

        if (result.hasErrors()) {

            System.out.println("errore");

            return "pages/projects/projectForm";

        } else {

            Project newProject = projectService.create(project);

            red.addFlashAttribute("msg", newProject.getName() + ", Progetto aggiunto correttamente");

            return "redirect:/projects";

        }

    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Integer id, Model projectM) {

        projectM.addAttribute("project", projectService.findById(id));

        return "pages/projects/projectForm";

    }

    @PutMapping("/{id}")
    public String updateProject(@PathVariable Integer id, @Validated @ModelAttribute("project") Project project,
            BindingResult result, RedirectAttributes red, Model projectM) {

        if (result.hasErrors()) {

            projectM.addAttribute("project", project);

            return "pages/projects/projectForm";

        } else {

            Project oldProject = projectService.findById(project.getId());

            if (oldProject.equals(project)) {

                red.addFlashAttribute("msg", project.getName() + ", Nessuna modifica apportata");

                return "redirect:/projects";

            }

            projectService.update(project);

            red.addFlashAttribute("msg", project.getName() + ", Progetto modificato correttamente");

            return "redirect:/projects";

        }

    }

    @DeleteMapping("/")
    public String deleteProject(@RequestParam("id") Integer id, RedirectAttributes red) {

        Project project = projectService.findById(id);

        if (project.getEmployees().size() > 0) {

            red.addFlashAttribute("msg", project.getName() + ", Eliminazione fallita, Dissocia i dipendenti!");

            return "redirect:/projects";

        }

        projectService.delete(id);

        red.addFlashAttribute("msg", project.getName() + ", Progetto eliminato correttamente");

        return "redirect:/projects";

    }

    @GetMapping("/management/{id}")
    public String manageProject(@PathVariable Integer id, Model projectM) {

        projectM.addAttribute("project", projectService.findById(id));

        // Con stream
        // projectM.addAttribute("assigned", empService.findByAssignedProjectS(id));
        // projectM.addAttribute("unassigned", empService.findByUnassignedProjectS(id));
        // projectM.addAttribute("projectId", id);

        // Con query
        projectM.addAttribute("assigned", empService.findByAssignedProjectQ(id));
        projectM.addAttribute("unassigned", empService.findByUnassignedProjectQ(id));
        projectM.addAttribute("projectId", id);

        return "pages/projects/assignment";

    }

    @PostMapping("/management/{eid}/{pid}")
    public String postMethodName(@PathVariable Integer eid, @PathVariable Integer pid, Model pojectM) {

        Employee emp = empService.findById(eid);

        Project project = projectService.findById(pid);

        emp.getProjects().add(project);

        empService.update(emp);

        return "redirect:/projects/management/" + project.getId();
    }

    @DeleteMapping("/management/{eid}/{pid}")
    public String unassignEmp(@PathVariable Integer eid, @PathVariable Integer pid, Model pojectM) {

        Employee emp = empService.findById(eid);

        Project project = projectService.findById(pid);

        emp.getProjects().remove(project);

        empService.update(emp);

        return "redirect:/projects/management/" + project.getId();

    }

}

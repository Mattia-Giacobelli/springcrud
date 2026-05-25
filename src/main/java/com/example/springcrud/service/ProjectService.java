package com.example.springcrud.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springcrud.entities.Project;
import com.example.springcrud.repositories.IProjectRepository;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final IProjectRepository projectRepo;

    public ProjectService(IProjectRepository projectRepo) {

        this.projectRepo = projectRepo;

    }

    public List<Project> index() {

        return projectRepo.findAll();

    }

    public Page<Project> findAll(Pageable pageable) {

        return projectRepo.findAll(pageable);
    }

    public Project findById(Integer id) {

        return projectRepo.findById(id).orElseThrow();

    }

    @Transactional
    public Project create(Project project) {

        return projectRepo.save(project);

    }

    @Transactional
    public Project update(Project project) {

        return projectRepo.save(project);

    }

    @Transactional
    public void delete(Integer id) {

        projectRepo.deleteById(id);

    }

}

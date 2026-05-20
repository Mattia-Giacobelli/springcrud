package com.example.springcrud.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.springcrud.entities.Permission;
import com.example.springcrud.repositories.IPermissionRepository;
import com.example.springcrud.utilities.PermissionType;

@Service
public class PermissionService {

    private final IPermissionRepository permissionRepo;

    public PermissionService(IPermissionRepository permissionRepo) {

        this.permissionRepo = permissionRepo;

    }

    public List<Permission> findAll() {

        return permissionRepo.findAll();

    }

    public Permission findByType(PermissionType permissionType) {

        return permissionRepo.findByPermissionType(permissionType).orElseThrow();

    }

}

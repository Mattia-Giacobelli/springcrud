package com.example.springcrud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springcrud.entities.Permission;
import java.util.Optional;

import com.example.springcrud.utilities.PermissionType;

public interface IPermissionRepository extends JpaRepository<Permission, Integer> {

    public Optional<Permission> findByPermissionType(PermissionType permissionType);

}

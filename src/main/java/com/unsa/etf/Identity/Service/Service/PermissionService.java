package com.unsa.etf.Identity.Service.Service;

import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getPermissions() {
        return permissionRepository.findAll();
    }


    public Permission getPermissionById(String permissionId) {
        if(permissionRepository.existsById(permissionId)) {
            return permissionRepository.findById(permissionId).get();
        }
        return null;
    }

    public Permission addNewPermission(Permission permission) {
        Optional<Permission> permissionOptional = permissionRepository.findPermissionByName(permission.getName());
        permissionRepository.save(permission);
        return permission;
    }

    public Permission updatePermission(Permission permission) {
        return permissionRepository.save(permission);
    }

    public boolean deletePermission(String permissionId) {
        if (permissionRepository.existsById(permissionId)) {
            permissionRepository.deleteById(permissionId);
            return true;
        }
        return false;
    }

    public void deleteAll() {
        permissionRepository.deleteAll();
    }
}

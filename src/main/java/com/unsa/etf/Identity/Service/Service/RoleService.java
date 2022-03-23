package com.unsa.etf.Identity.Service.Service;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role addNewRole(Role role) {
        Optional<Role> roleOptional = roleRepository.findRoleByName(role.getName());
        if(roleOptional.isPresent()) {
            return null;
        }
        roleRepository.save(role);
        return role;
    }

    public boolean deleteRole(String roleId) {
        if (roleRepository.existsById(roleId)) {
            roleRepository.deleteById(roleId);
            return true;
        }
        return false;
    }

    public Role updateRole(Role role) {
        if (roleRepository.existsById(role.getId())) {
            return roleRepository.save(role);
        }
        return null;
    }

//    public void deleteAllRoles() {
//        roleRepository.deleteAll();
//    }
}

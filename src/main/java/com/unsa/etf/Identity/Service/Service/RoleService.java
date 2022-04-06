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
    private final UserService userService;
    @Autowired
    public RoleService(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
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
            Role role = roleRepository.getById(roleId);

            userService.setUserRoleToNull(role);

            roleRepository.deleteById(roleId);
            return true;
        }
        return false;
    }

    public boolean deleteAll() {
        userService.setAllRolesToNull();
        roleRepository.deleteAll();
        return true;
    }

    public Role updateRole(Role role) {
        return roleRepository.save(role);
    }

    public Role getRoleById(String roleId) {
        if(roleRepository.existsById(roleId)) {
            return roleRepository.findById(roleId).get();
        }
        return null;
    }
}

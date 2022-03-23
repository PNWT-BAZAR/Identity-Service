package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Service.RoleService;
import com.unsa.etf.Identity.Service.Validator.IdentityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;
    private final IdentityValidator identityValidator;

    @Autowired
    public RoleController(RoleService roleService, IdentityValidator identityValidator) {
        this.roleService = roleService;
        this.identityValidator = identityValidator;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        if (identityValidator.isValid(role)) {
            Role role1 = roleService.addNewRole(role);
            if (role1 == null) {
                return ResponseEntity.status(409).body("Role Already Exists!");
            }
            return ResponseEntity.status(200).body(role1);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(role));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") String roleId) {
        if (roleService.deleteRole(roleId)) {
            return ResponseEntity.status(200).body("Role Successfully Deleted!");
        }
        return ResponseEntity.status(409).body("Role Does Not Exist!");
    }

//    @DeleteMapping("role/deleteAll")
//    public ResponseEntity<?> deleteAllRoles() {
//        roleService.deleteAllRoles();
//        return ResponseEntity.status(200).body("Roles Successfully Deleted!");
//    }

    @PutMapping
    public ResponseEntity<?> updateRole(@RequestBody Role role) {
        if (identityValidator.isValid(role)) {
            Role updatedRole = roleService.updateRole(role);
            if (updatedRole == null) {
                return ResponseEntity.status(409).body("Role Does Not Exist!");
            }
            return ResponseEntity.status(200).body(updatedRole);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(role));
    }

}

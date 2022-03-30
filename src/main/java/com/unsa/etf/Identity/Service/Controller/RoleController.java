package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Service.RoleService;
import com.unsa.etf.Identity.Service.Validator.BadRequestResponseBody;
import com.unsa.etf.Identity.Service.Validator.BodyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;
    private final BodyValidator bodyValidator;

    @Autowired
    public RoleController(RoleService roleService, BodyValidator bodyValidator) {
        this.roleService = roleService;
        this.bodyValidator = bodyValidator;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getRoles();
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<?> getRoleById(@PathVariable("roleId") String roleId) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            return ResponseEntity.status(409).body(new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Role Does Not Exist!"));
        }
        return ResponseEntity.status(200).body(role);
    }

    @PostMapping
    public ResponseEntity<?> createRole(@RequestBody Role role) {
        if (bodyValidator.isValid(role)) {
            Role role1 = roleService.addNewRole(role);
            if (role1 == null) {
                return ResponseEntity.status(409).body(new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.ALREADY_EXISTS, "Role Already Exists!"));
            }
            return ResponseEntity.status(200).body(role1);
        }
        return ResponseEntity.status(409).body(bodyValidator.determineConstraintViolation(role));
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") String roleId) {
        if (roleService.deleteRole(roleId)) {
            return ResponseEntity.status(200).body("Role Successfully Deleted!");
        }
        return ResponseEntity.status(409).body(new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Role Does Not Exist!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        roleService.deleteAll();
        return ResponseEntity.status(200).body("Roles Successfully Deleted!");
    }

    @PutMapping
    public ResponseEntity<?> updateRole(@RequestBody Role role) {
        if (bodyValidator.isValid(role)) {
            Role updatedRole = roleService.updateRole(role);
            return ResponseEntity.status(200).body(updatedRole);
        }
        return ResponseEntity.status(409).body(bodyValidator.determineConstraintViolation(role));
    }

}

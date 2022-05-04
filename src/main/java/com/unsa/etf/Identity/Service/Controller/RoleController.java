package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Responses.ObjectDeletionResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectListResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectResponse;
import com.unsa.etf.Identity.Service.Service.RoleService;
import com.unsa.etf.Identity.Service.Responses.BadRequestResponseBody;
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
    public ObjectListResponse<Role> getRoles() {
        return new ObjectListResponse<>(200, roleService.getRoles(), null);
    }

    @GetMapping("/{roleId}")
    public ObjectResponse<Role> getRoleById(@PathVariable("roleId") String roleId) {
        Role role = roleService.getRoleById(roleId);
        if (role == null) {
            return new ObjectResponse<>(409, null,
                    new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Role Does Not Exist!"));
        }
        return new ObjectResponse<>(200, role, null);
    }

    @PostMapping
    public ObjectResponse<Role> createRole(@RequestBody Role role) {
        if (bodyValidator.isValid(role)) {
            Role role1 = roleService.addNewRole(role);
            if (role1 == null) {
                return new ObjectResponse<>(409, null,
                        new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.ALREADY_EXISTS, "Role Already Exists!"));
            }
            return new ObjectResponse<>(200, role1, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(role));
    }

    @DeleteMapping("/{roleId}")
    public ObjectDeletionResponse deleteRole(@PathVariable("roleId") String roleId) {
        if (roleService.deleteRole(roleId)) {
            return new ObjectDeletionResponse(200, "Role Successfully Deleted!", null);
        }
        return new ObjectDeletionResponse(409, "Error has occurred!",
                new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Role Does Not Exist!"));
    }

    @DeleteMapping
    public ObjectDeletionResponse deleteAll() {
        roleService.deleteAll();
        return new ObjectDeletionResponse(200, "Roles Successfully Deleted!", null);
    }

    @PutMapping
    public ObjectResponse<Role> updateRole(@RequestBody Role role) {
        if (bodyValidator.isValid(role)) {
            Role updatedRole = roleService.updateRole(role);
            return new ObjectResponse<>(200, updatedRole, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(role));
    }

}

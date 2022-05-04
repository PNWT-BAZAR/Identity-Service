package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Responses.ObjectDeletionResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectListResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectResponse;
import com.unsa.etf.Identity.Service.Service.PermissionService;
import com.unsa.etf.Identity.Service.Responses.BadRequestResponseBody;
import com.unsa.etf.Identity.Service.Validator.BodyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final BodyValidator bodyValidator;

    @Autowired
    public PermissionController(PermissionService permissionService, BodyValidator bodyValidator) {
        this.permissionService = permissionService;
        this.bodyValidator = bodyValidator;
    }

    @GetMapping
    public ObjectListResponse<Permission> getPermissions() {
        return new ObjectListResponse<>(200, permissionService.getPermissions(), null);
    }

    @GetMapping("/{permissionId}")
    public ObjectResponse<Permission> getPermissionById(@PathVariable("permissionId") String permissionId) {
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            return new ObjectResponse<>(409, null,
                    new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Permission Does Not Exist!"));
        }
        return new ObjectResponse<>(200, permission, null);
    }

    @GetMapping("/name/{name}")
    public ObjectListResponse<Permission> getPermissionWithName(@PathVariable("name") String name) {
        List<Permission> permissions = permissionService.getPermissionWithName(name);
        return new ObjectListResponse<>(200, permissions, null);
    }

    @PostMapping
    public ObjectResponse<Permission> createPermission(@RequestBody Permission permission){
        if (bodyValidator.isValid(permission)) {
            Permission permission1 = permissionService.addNewPermission(permission);
            return new ObjectResponse<>(200, permission1, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(permission));
    }

    @PutMapping
    public ObjectResponse<Permission> updatePermission(@RequestBody Permission permission) {
        if (bodyValidator.isValid(permission)) {
            Permission updatedPermission = permissionService.updatePermission(permission);
            return new ObjectResponse<>(200, updatedPermission, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(permission));
    }

    @DeleteMapping("/{permissionId}")
    public ObjectDeletionResponse deletePermission(@PathVariable("permissionId") String permissionId) {
        if (permissionService.deletePermission(permissionId)) {
            return new ObjectDeletionResponse(200, "Permission Successfully Deleted!", null);
        }
        return new ObjectDeletionResponse(409, "Error has occurred!",
                new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Permission Does Not Exist!"));
    }

    @DeleteMapping
    public ObjectDeletionResponse deleteAll() {
        permissionService.deleteAll();
        return new ObjectDeletionResponse(200, "Permissions Successfully Deleted!", null);
    }
}

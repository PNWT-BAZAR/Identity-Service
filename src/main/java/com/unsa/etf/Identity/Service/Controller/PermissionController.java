package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Service.PermissionService;
import com.unsa.etf.Identity.Service.Validator.BadRequestResponseBody;
import com.unsa.etf.Identity.Service.Validator.IdentityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("permissions")
public class PermissionController {

    private final PermissionService permissionService;
    private final IdentityValidator identityValidator;

    @Autowired
    public PermissionController(PermissionService permissionService, IdentityValidator identityValidator) {
        this.permissionService = permissionService;
        this.identityValidator = identityValidator;
    }

    @GetMapping
    public List<Permission> getPermissions() {
        return permissionService.getPermissions();
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<?> getPermissionById(@PathVariable("permissionId") String permissionId) {
        Permission permission = permissionService.getPermissionById(permissionId);
        if (permission == null) {
            return ResponseEntity.status(409).body(new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Permission Does Not Exist!"));
        }
        return ResponseEntity.status(200).body(permission);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<?> getPermissionWithName(@PathVariable("name") String name) {
        List<Permission> permissions = permissionService.getPermissionWithName(name);
        return ResponseEntity.status(200).body(permissions);
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody Permission permission){
        if (identityValidator.isValid(permission)) {
            Permission permission1 = permissionService.addNewPermission(permission);
            return ResponseEntity.status(200).body(permission1);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(permission));
    }

    @PutMapping
    public ResponseEntity<?> updatePermission(@RequestBody Permission permission) {
        if (identityValidator.isValid(permission)) {
            Permission updatedPermission = permissionService.updatePermission(permission);
            return ResponseEntity.status(200).body(updatedPermission);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(permission));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> deletePermission(@PathVariable("permissionId") String permissionId) {
        if (permissionService.deletePermission(permissionId)) {
            return ResponseEntity.status(200).body("Permission Successfully Deleted!");
        }
        return ResponseEntity.status(409).body(new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "Permission Does Not Exist!"));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        permissionService.deleteAll();
        return ResponseEntity.status(200).body("Permissions Successfully Deleted!");
    }
}

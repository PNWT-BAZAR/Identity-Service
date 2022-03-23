package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.Permission;
import com.unsa.etf.Identity.Service.Service.PermissionService;
import com.unsa.etf.Identity.Service.Validator.IdentityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
            return ResponseEntity.status(409).body("Permission Does Not Exist!");
        }
        return ResponseEntity.status(200).body(permission);
    }

    @PostMapping
    public ResponseEntity<?> createPermission(@RequestBody Permission permission){
        if (identityValidator.isValid(permission)) {
            Permission permission1 = permissionService.addNewPermission(permission);
            if (permission1 == null) {
                return ResponseEntity.status(409).body("Permission Already Exists!");
            }
            return ResponseEntity.status(200).body(permission1);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(permission));
    }

    @PutMapping
    public ResponseEntity<?> updatePermission(@RequestBody Permission permission) {
        if (identityValidator.isValid(permission)) {
            Permission updatedPermission = permissionService.updatePermission(permission);
            if (updatedPermission == null) {
                return ResponseEntity.status(409).body("Permission Does Not Exist!");
            }
            return ResponseEntity.status(200).body(updatedPermission);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(permission));
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<?> deletePermision(@PathVariable("permissionId") String permissionId) {
        if (permissionService.deletePermission(permissionId)) {
            return ResponseEntity.status(200).body("Permission Successfully Deleted!");
        }
        return ResponseEntity.status(409).body("Permission Does Not Exist!");
    }
}

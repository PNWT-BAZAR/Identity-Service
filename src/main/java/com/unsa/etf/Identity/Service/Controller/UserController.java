package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Service.UserService;
import com.unsa.etf.Identity.Service.Validator.IdentityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final IdentityValidator identityValidator;

    @Autowired
    public UserController(UserService userService, IdentityValidator identityValidator) {
        this.userService = userService;
        this.identityValidator = identityValidator;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") String userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.status(409).body("User Does Not Exist!");
        }
        return ResponseEntity.status(200).body(user);
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (identityValidator.isValid(user)) {
            User user1 = userService.addNewUser(user);
            if (user1 == null) {
                return ResponseEntity.status(409).body("User Already Exists!");
            }
            return ResponseEntity.status(200).body(user1);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") String userId) {
        if(userService.deleteUserById(userId)){
            return ResponseEntity.status(200).body("User Successfully Deleted!");
        }
        return ResponseEntity.status(409).body("User Does Not Exist!");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll() {
        userService.deleteAllUsers();
        return ResponseEntity.status(200).body("Users Successfully Deleted!");
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user) {
        if (identityValidator.isValid(user)) {
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.status(200).body(updatedUser);
        }
        return ResponseEntity.status(409).body(identityValidator.determineConstraintViolation(user));
    }
}

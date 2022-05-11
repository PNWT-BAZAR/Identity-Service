package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Requests.LoginRequest;
import com.unsa.etf.Identity.Service.Responses.ObjectDeletionResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectListResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectResponse;
import com.unsa.etf.Identity.Service.Service.UserService;
import com.unsa.etf.Identity.Service.Validator.BodyValidator;
import com.unsa.etf.Identity.Service.Responses.BadRequestResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final BodyValidator bodyValidator;

    @Autowired
    public UserController(UserService userService, BodyValidator bodyValidator) {
        this.userService = userService;
        this.bodyValidator = bodyValidator;
    }

    @GetMapping
    public ObjectListResponse<User> getUsers() {
        return new ObjectListResponse<>(200, userService.getUsers(), null);
    }

    @GetMapping("/{userId}")
    public ObjectResponse<User> getUserById(@PathVariable("userId") String userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ObjectResponse<>(409,null,
                    new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "User Does Not Exist!"));
        }
        return new ObjectResponse<>(200, user, null);
    }

    @GetMapping("/name/{name}")
    public ObjectListResponse<User> getUserByName(@PathVariable("name") String name) {
        List<User> users = userService.getUsersByName(name);
        return new ObjectListResponse<>(200, users, null);
    }

    @GetMapping("/sort")
    public ObjectListResponse<User> sortByLastName() {
        List<User> users = userService.sortByLastName();
        return new ObjectListResponse<>(200, users, null);
    }

    @PostMapping
    public ObjectResponse<User> createUser(@RequestBody User user) {
        if (bodyValidator.isValid(user)) {
            User user1 = userService.addNewUser(user);
            if (user1 == null) {
                return new ObjectResponse<>(409, null,
                        new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.ALREADY_EXISTS, "User Already Exists!"));
            }
            return new ObjectResponse<>(200, user1, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(user));
    }

    @DeleteMapping("/{userId}")
    public ObjectDeletionResponse deleteUser(@PathVariable("userId") String userId) {
        if(userService.deleteUserById(userId)){
            return new ObjectDeletionResponse(200, "User Successfully Deleted!", null);
        }
        return new ObjectDeletionResponse(409, "Error has occurred!",
                new BadRequestResponseBody(BadRequestResponseBody.ErrorCode.NOT_FOUND, "User Does Not Exist!"));
    }

    @DeleteMapping
    public ObjectDeletionResponse deleteAll() {
        userService.deleteAllUsers();
        return new ObjectDeletionResponse(200, "Users Successfully Deleted!", null);
    }

    @PutMapping
    public ObjectResponse<User> updateUser(@RequestBody User user) {
        if (bodyValidator.isValid(user)) {
            User updatedUser = userService.updateUser(user);
            return new ObjectResponse<>(200, updatedUser, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(user));
    }

    @PostMapping("/login")
    public void login (@RequestBody LoginRequest user) {
        System.out.println("/login");
    }
}

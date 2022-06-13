package com.unsa.etf.Identity.Service.Controller;

import com.unsa.etf.Identity.Service.Model.RoleEnum;
import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Requests.LoginRequest;
import com.unsa.etf.Identity.Service.Requests.SignupRequest;
import com.unsa.etf.Identity.Service.Responses.BadRequestResponseBody.ErrorCode;
import com.unsa.etf.Identity.Service.Responses.ObjectDeletionResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectListResponse;
import com.unsa.etf.Identity.Service.Responses.ObjectResponse;
import com.unsa.etf.Identity.Service.Security.jwtutil.JwtUtil;
import com.unsa.etf.Identity.Service.Service.UserService;
import com.unsa.etf.Identity.Service.Validator.BodyValidator;
import com.unsa.etf.Identity.Service.Responses.BadRequestResponseBody;
import com.unsa.etf.Identity.Service.rabbitmq.RabbitMessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final BodyValidator bodyValidator;
    private final RabbitMessageSender rabbitMessageSender;


    @GetMapping
    public ObjectListResponse<User> getUsers() {
        return new ObjectListResponse<>(200, userService.getUsers(), null);
    }

    @GetMapping("/{userId}")
    public ObjectResponse<User> getUserById(@PathVariable("userId") String userId){
        User user = userService.getUserById(userId);
        if (user == null) {
            return new ObjectResponse<>(409,null,
                    new BadRequestResponseBody(ErrorCode.NOT_FOUND, "User Does Not Exist!"));
        }
        return new ObjectResponse<>(200, user, null);
    }

    @GetMapping("/username")
    public ObjectResponse<User> getUserByUsername(@RequestParam String username){
        User user = userService.findByUsername(username);
        if(user == null){
            return new ObjectResponse<>(409,null,
                    new BadRequestResponseBody(ErrorCode.NOT_FOUND, "User Does Not Exist!"));
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
            rabbitMessageSender.notifyOrderServiceOfChange(user1, "add");
            if (user1 == null) {
                return new ObjectResponse<>(409, null,
                        new BadRequestResponseBody(ErrorCode.ALREADY_EXISTS, "User Already Exists!"));
            }
            return new ObjectResponse<>(200, user1, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(user));
    }

    @DeleteMapping("/{userId}")
    public ObjectDeletionResponse deleteUser(@PathVariable("userId") String userId) {
        var userToBeDeleted = userService.getUserById(userId);
        if(userService.deleteUserById(userId)){
            rabbitMessageSender.notifyOrderServiceOfChange(userToBeDeleted, "delete");
            return new ObjectDeletionResponse(200, "User Successfully Deleted!", null);
        }
        return new ObjectDeletionResponse(409, "Error has occurred!",
                new BadRequestResponseBody(ErrorCode.NOT_FOUND, "User Does Not Exist!"));
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
            rabbitMessageSender.notifyOrderServiceOfChange(updatedUser, "update");
            return new ObjectResponse<>(200, updatedUser, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(user));
    }

    @PostMapping("/login")
    public ObjectResponse<String> login (@RequestBody LoginRequest user) {
        System.out.println("/login");
        return new ObjectResponse<>(200, JwtUtil.JWT_TOKEN, null);
    }

    @PostMapping("/signup")
    public ObjectResponse<User> signup (@RequestBody SignupRequest signupRequest){
        var userTmp = convertSignupRequestToUserModel(signupRequest);
        if(bodyValidator.isValid(userTmp)){
            if(userService.existsByUsername(signupRequest.getUsername())){
                return new ObjectResponse<>(409, null, new BadRequestResponseBody(ErrorCode.ALREADY_EXISTS, "Username is already taken!"));
            }
            if(userService.existsByEmail(signupRequest.getEmail())){
                return new ObjectResponse<>(409, null, new BadRequestResponseBody(ErrorCode.ALREADY_EXISTS, "Email is already taken!"));
            }
            var createdUser = userService.signup(signupRequest);
            rabbitMessageSender.notifyOrderServiceOfChange(createdUser, "add");
            return new ObjectResponse<>(200, createdUser, null);
        }
        return new ObjectResponse<>(409, null, bodyValidator.determineConstraintViolation(userTmp));
    }

    private User convertSignupRequestToUserModel (SignupRequest signupRequest){
        return User.builder().firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .passwordHash(signupRequest.getPassword())
                .phoneNumber(signupRequest.getPhoneNumber())
                .shippingAddress(signupRequest.getShippingAddress())
                .build();
    }
}

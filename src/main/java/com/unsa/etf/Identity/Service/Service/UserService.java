package com.unsa.etf.Identity.Service.Service;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Model.RoleEnum;
import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Repository.UserRepository;
import com.unsa.etf.Identity.Service.Requests.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
//    private final PasswordEncoder passwordEncoder;

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String userId) {
        if(userRepository.existsById(userId)) {
            return userRepository.findById(userId).get();
        }
        return null;
    }

    public boolean deleteUserById(String userId) {
        if(userRepository.existsById(userId)){
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    public boolean deleteAllUsers() {
        userRepository.deleteAll();
        return true;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User addNewUser(User user) {
        Optional<User> userOptional = userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        if(userOptional.isPresent()) {
            return null;
        }
//        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        userRepository.save(user);
        return user;
    }

    public void setUserRoleToNull(Role role) {
        List<User> users = userRepository.findUsersByRole(role);
        if (!users.isEmpty()) {
            for (User user : users) {
                user.setRole(null);
            }
            userRepository.saveAll(users);
        }
    }

    public void setAllRolesToNull() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            user.setRole(null);
        }
        userRepository.saveAll(users);
    }

    public List<User> getUsersByName(String name) {
        return userRepository.findUsersByFirstOrLastName(name);
    }

    public List<User> sortByLastName() {
        return userRepository.orderByLastName();
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public User signup (SignupRequest signupRequest){
        var newUser = User.builder().firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .passwordHash(encoder.encode(signupRequest.getPassword()))
                .phoneNumber(signupRequest.getPhoneNumber())
                .shippingAddress(signupRequest.getShippingAddress())
                // TODO: 24.05.2022. provjeri koju rolu ovdje stavit, dodati role u putanje, dodati rabbit na identitz servis
                .role(signupRequest.getRole())
                .build();

        userRepository.save(newUser);
        return newUser;
    }

    public String getPasswordHashForUsername(String username){
        return userRepository.findByUsername(username).getPasswordHash();
    }
}

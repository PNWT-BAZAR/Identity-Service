package com.unsa.etf.Identity.Service.Service;

import com.unsa.etf.Identity.Service.Model.User;
import com.unsa.etf.Identity.Service.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public User updateUser(User user) {
        if (userRepository.existsById(user.getId())) {
            return userRepository.save(user);
        }
        return null;
    }

    public User addNewUser(User user) {
        Optional<User> userOptional = userRepository.findUserByUsernameOrEmail(user.getUsername(), user.getEmail());
        if(userOptional.isPresent()) {
            return null;
        }
        userRepository.save(user);
        return user;
    }
}

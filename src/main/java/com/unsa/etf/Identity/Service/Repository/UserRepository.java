package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.Model.Role;
import com.unsa.etf.Identity.Service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?2")
    Optional<User> findUserByUsernameOrEmail(String username, String email);

    @Query("SELECT u FROM User u WHERE u.role = ?1")
    List<User> findUsersByRole(Role role);

    @Query("SELECT u FROM User u WHERE concat(u.firstName, ' ', u.lastName) LIKE concat('%', ?1, '%')")
    List<User> findUsersByFirstOrLastName(String name);

    @Query("SELECT u FROM User u ORDER BY u.lastName")
    List<User> orderByLastName();
}

package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    @Query("SELECT r FROM Role r WHERE r.name = ?1")
    Optional<Role> findRoleByName(String name);
}

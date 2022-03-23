package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, String> {

    @Query("SELECT p FROM Permission p WHERE p.name = ?1")
    Optional<Permission> findPermissionByName(String name);
}

package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {

}

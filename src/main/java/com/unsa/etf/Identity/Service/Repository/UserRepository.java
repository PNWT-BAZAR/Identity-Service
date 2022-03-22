package com.unsa.etf.Identity.Service.Repository;

import com.unsa.etf.Identity.Service.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}

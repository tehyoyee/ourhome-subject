package com.ourhome.taehyeong.repository;

import com.ourhome.taehyeong.entities.User;
import com.ourhome.taehyeong.entities.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findUserByUsername(String username);

    Optional<Role> findRoleByUsername(String username);

}

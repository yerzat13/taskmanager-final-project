package com.taskmanager.repository;

import com.taskmanager.entity.AbdrassulayevYerzatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbdrassulayevYerzatUserRepository extends JpaRepository<AbdrassulayevYerzatUser, Long> {

    Optional<AbdrassulayevYerzatUser> findByUsername(String username);

    Optional<AbdrassulayevYerzatUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
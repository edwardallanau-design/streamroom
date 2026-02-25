package com.streamroom.repository;

import com.streamroom.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByTwitchUsername(String twitchUsername);

    Optional<User> findFirstByIsAdminTrue();

    boolean existsByIsAdminTrue();
}

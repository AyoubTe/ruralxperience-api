package com.ruralxperience.repository;

import com.ruralxperience.entity.User;
import com.ruralxperience.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    Page<User> findByRole(Role role, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.enabled = true AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           " LOWER(u.lastName) LIKE LOWER(CONCAT('%',:q,'%')) OR " +
           " LOWER(u.email) LIKE LOWER(CONCAT('%',:q,'%')))")
    Page<User> searchUsers(String q, Pageable pageable);

    long countByRole(Role role);
}

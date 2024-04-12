package com.yvolabs.hotelservice.repository;

import com.yvolabs.hotelservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Yvonne N
 */
public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String role);

    boolean existsByName(String roleName);
}

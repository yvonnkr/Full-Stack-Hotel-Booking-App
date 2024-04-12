package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.model.Role;
import com.yvolabs.hotelservice.model.User;

import java.util.List;

/**
 * @author Yvonne N
 */
public interface IRoleService {
    List<Role> getRoles();

    Role createRole(Role theRole);

    void deleteRole(Long id);

    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);

    User assignRoleToUser(Long userId, Long roleId);

    Role removeAllUsersFromRole(Long roleId);
}

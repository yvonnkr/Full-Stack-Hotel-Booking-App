package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.exception.ResourceNotFoundException;
import com.yvolabs.hotelservice.exception.RoleAlreadyExistException;
import com.yvolabs.hotelservice.exception.UserAlreadyExistsException;
import com.yvolabs.hotelservice.model.Role;
import com.yvolabs.hotelservice.model.User;
import com.yvolabs.hotelservice.repository.RoleRepository;
import com.yvolabs.hotelservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Yvonne N
 */
@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role createRole(Role theRole) {
        String roleName = "ROLE_" + theRole.getName().toUpperCase();
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)) {
            throw new RoleAlreadyExistException(theRole.getName() + " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + name));

    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (roleOpt.isPresent() && userOpt.isPresent()) {
            Role role = roleOpt.get();
            User user = userOpt.get();
            if (role.getUsers().contains(user)) {
                role.removeUserFromRole(user);
                roleRepository.save(role);
                return user;
            }
        }

        throw new UsernameNotFoundException("User not found");

    }


    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Role> roleOpt = roleRepository.findById(roleId);

        if (userOpt.isPresent() && roleOpt.isPresent()) {
            Role role = roleOpt.get();
            User user = userOpt.get();

            if (user.getRoles().contains(role)) {
                throw new UserAlreadyExistsException(
                        user.getFirstName() + " is already assigned to the " + role.getName() + " role");
            }

            role.assignRoleToUser(user);
            roleRepository.save(role);
            return user;
        }
        return null;
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {

        Optional<Role> roleOpt = roleRepository.findById(roleId);
        if (roleOpt.isPresent()) {
            Role role = roleOpt.get();
            role.removeAllUsersFromRole();
            return roleRepository.save(role);
        }
        return null;

    }
}

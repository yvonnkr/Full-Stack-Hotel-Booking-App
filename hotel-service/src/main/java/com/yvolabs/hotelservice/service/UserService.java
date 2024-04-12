package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.exception.UserAlreadyExistsException;
import com.yvolabs.hotelservice.model.Role;
import com.yvolabs.hotelservice.model.User;
import com.yvolabs.hotelservice.repository.RoleRepository;
import com.yvolabs.hotelservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Yvonne N
 */
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //By default, on creation user will be assigned: "ROLE_USER"
        Role userRole = roleRepository.findByName("ROLE_USER").orElse(null);
        user.setRoles(Collections.singleton(userRole));

        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User user = getUser(email);
        if (user != null) {
            userRepository.deleteByEmail(email);
        }
    }


    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}

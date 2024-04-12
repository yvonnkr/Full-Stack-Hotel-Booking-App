package com.yvolabs.hotelservice.service;

import com.yvolabs.hotelservice.model.User;

import java.util.List;

/**
 * @author Yvonne N
 */
public interface IUserService {
    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}

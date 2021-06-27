package com.scooterrental.scooter_rental.security.service;

import com.scooterrental.scooter_rental.security.model.User;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAllUsers();

    User findByUsername(String username);

    User findById(Long userId);

    void deleteUser(Long userId);


}

package com.scooterrental.scooter_rental.security.service;

import com.scooterrental.scooter_rental.security.model.Role;
import com.scooterrental.scooter_rental.security.model.Status;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.repository.RoleRepository;
import com.scooterrental.scooter_rental.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //todo EXCEPTIONS !

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        Role roleUser = roleRepository.findByName("ROLE_USER");
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setStatus(Status.ACTIVE);

        User registeredUser = userRepository.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = userRepository.findAll();
        log.info("IN getAllUsers - {} users found", usersList.size());
        return usersList;
    }

    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    @Override
    public User findById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("IN findById - no user found by id: {}", userId);
            return null;
        }
        log.info("IN findById - user: {} found by id: {}", user, userId);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("IN delete - no such user with id: {} in database", userId);
            throw new IllegalStateException("No user with id: " + userId + " in database");
        }
        userRepository.deleteById(userId);
        log.info("IN delete - user with id: {} successfully deleted", userId);
    }
}

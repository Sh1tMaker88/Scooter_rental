package com.scooterrental.scooter_rental.security.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.security.dto.UserDto;
import com.scooterrental.scooter_rental.security.model.Role;
import com.scooterrental.scooter_rental.security.model.Status;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.repository.RoleRepository;
import com.scooterrental.scooter_rental.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

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
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> getAllUsers() {
        List<User> usersList = userRepository.findAll();
        log.info("IN getAllUsers - {} users was found", usersList.size());
        return usersList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->  new ServiceException("IN findByUsername - user with username " + username +
                        " was not found"));
        log.info("IN findByUsername - user: {} found by username: {}", user, username);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public User findById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            log.warn("IN findById - no user found by id: {}", userId);
            throw new ServiceException("IN findById - no user found by id: " + userId);
        }
        log.info("IN findById - user: {} found by id: {}", user, userId);
        return user;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            log.warn("IN deleteUser - no such user with id: {} in database", userId);
            throw new ServiceException("No user with id: " + userId + " in database");
        }
        userRepository.deleteById(userId);
        log.info("IN deleteUser - user with id: {} successfully deleted", userId);
    }

    @Override
    @Transactional
    public User updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->  new ServiceException("IN findByUsername - user id: " + id +
                " was not found"));
        return checkAndSetUserFields(userDto, user);
    }

    private User checkAndSetUserFields(UserDto userDto, User user) {
        if (!userDto.getUsername().equals(user.getUsername())) {
            if (userDto.getUsername() != null && userDto.getUsername().length() > 2 &&
                    userRepository.findByUsername(userDto.getUsername()).isEmpty()) {
                user.setUsername(userDto.getUsername());
            } else {
                log.warn("Incorrect username input or it already exists");
                throw new ServiceException("Incorrect username input or it already exists");
            }
        }

        if (userDto.getFirstName() != null && userDto.getFirstName().length() > 2) {
            user.setFirstName(userDto.getFirstName());
        } else {
            log.warn("Incorrect first name input or it already exists");
            throw new ServiceException("Incorrect first name input");
        }

        if (userDto.getLastName() != null && userDto.getLastName().length() > 2) {
            user.setLastName(userDto.getLastName());
        } else {
            log.warn("Incorrect last name input");
            throw new ServiceException("Incorrect last name input");
        }

        if (userDto.getEmail() != null && userRepository.findByEmail(userDto.getEmail()).isPresent() &&
                !userRepository.findByEmail(userDto.getEmail()).get().getUsername().equals(userDto.getUsername())) {
            log.warn("Email " + userDto.getEmail() + " already taken by another user");
            throw new ServiceException("Email " + userDto.getEmail() + " already taken by another user");
        } else {
            user.setEmail(userDto.getEmail());
        }

        if (userDto.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        } else {
            log.warn("You must enter password");
            throw new ServiceException("Password was empty");
        }
        user.setUpdated(new Date());


        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Page<User> getAllUsersWithPagination(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber - 1, pageSize, Sort.by(sortBy));
        Page<User> pagedResult = userRepository.findAll(paging);

        log.info("IN getAllUsers - {} users found", pagedResult.getTotalElements());
        return pagedResult;
    }
}

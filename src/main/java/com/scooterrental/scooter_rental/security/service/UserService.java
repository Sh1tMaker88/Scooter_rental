package com.scooterrental.scooter_rental.security.service;

import com.scooterrental.scooter_rental.security.dto.UserDto;
import com.scooterrental.scooter_rental.security.model.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {

    User register(User user);

    List<User> getAllUsers();

    User findByUsername(String username);

    User findById(Long userId);

    void deleteUser(Long userId);

    User updateUserWithDto(Long id, UserDto user);

    User saveUser(User user);

    Page<User> getAllUsersWithPagination(Integer pageNumber, Integer pageSize, String sortBy);
}

package com.scooterrental.scooter_rental.security.repository;

import com.scooterrental.scooter_rental.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

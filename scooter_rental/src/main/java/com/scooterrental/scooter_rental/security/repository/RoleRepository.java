package com.scooterrental.scooter_rental.security.repository;

import com.scooterrental.scooter_rental.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}

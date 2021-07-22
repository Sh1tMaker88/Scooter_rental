package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.Scooter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScooterRepository extends JpaRepository<Scooter, Long> {

    List<Scooter> findAllByRentalPointId(Long rentalPointId);

    Optional<Scooter> getScooterById(Long scooterId);

    void deleteScooterById(Long scooterId);
}

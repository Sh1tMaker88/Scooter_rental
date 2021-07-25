package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.RentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {

    @Query(value = "SELECT * FROM rent WHERE rental_point_id=?1", nativeQuery = true)
    List<RentHistory> findAllRentHistoryByRentalPointId(Long rentalPointId);

//    List<RentHistory> findAllByUsername(String username);

    @Query(value = "SELECT * FROM rent WHERE scooter_id=?1", nativeQuery = true)
    List<RentHistory> findAllByScooterId(Long scooterId);

    Optional<RentHistory> getRentHistoryById(Long rentHistoryId);
}

package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RentHistoryRepository extends JpaRepository<RentHistory, Long> {

    Page<RentHistory> findAllRentHistoryByRentalPointId(Pageable pageable, RentalPoint rentalPointId);

    Page<RentHistory> findAllByUserId(Pageable pageable, User userId);

    Page<RentHistory> findAllByScooterId(Pageable pageable, Scooter scooterId);

    Optional<RentHistory> getRentHistoryById(Long rentHistoryId);
}

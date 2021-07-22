package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.RentalPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalPointRepository extends JpaRepository<RentalPoint, Long> {

    List<RentalPoint> findAllByCityId(Long cityId);

    Integer countRentalPointByCityId(Long cityId);

    Optional<RentalPoint> getRentalPointById(Long rentalPointId);

    void deleteRentalPointById(Long rentalPointId);
}

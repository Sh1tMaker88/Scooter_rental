package com.scooterrental.scooter_rental.repository;

import com.scooterrental.scooter_rental.model.RentalPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RentalPriceRepository extends JpaRepository<RentalPrice, Long> {

    Optional<RentalPrice> getRentalPriceById(Long rentalPriceId);

    void deleteRentalPriceById(Long rentalPriceId);


}

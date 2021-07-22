package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.RentalPrice;

import java.util.List;

public interface RentalPriceService {

    RentalPrice getRentalPriceById(Long rentalPriceId);

    List<RentalPrice> findAllRentalPrice();

    void deleteRentalPriceById(Long rentalPriceId);

    RentalPrice saveRentalPrice(RentalPrice rentalPrice);
}

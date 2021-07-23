package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.Scooter;

import java.util.List;

public interface RentalPriceService {

    RentalPrice getRentalPriceById(Long rentalPriceId);

    List<RentalPrice> findAllRentalPrice();

    void deleteRentalPriceById(Long rentalPriceId);

    RentalPrice saveRentalPrice(RentalPrice rentalPrice);

    public Scooter setNewRentalPriceToScooter(RentalPrice rentalPrice, Long scooterId);

    public Scooter setPriceForScooterByPriceId(Long priceId, Long scooterId);
}

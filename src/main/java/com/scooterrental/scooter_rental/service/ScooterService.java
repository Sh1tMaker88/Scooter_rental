package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Scooter;

import java.util.List;

public interface ScooterService {

    List<Scooter> getAllScooters(Long rentalPointId);

    Scooter getScooterById(Long scooterId);

    void deleteScooterByID(Long scooterId);

    Scooter saveScooter(Scooter scooter);

    Scooter setNotAvailableStatus(Long scooterId);

    Scooter setAvailableStatus(Long scooterId);

    void checkIsScooterInRentalPoint(Long scooterId, Long rentalPointId);
}

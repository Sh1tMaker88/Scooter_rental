package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Scooter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ScooterService {

    Page<Scooter> getAllScootersOfRentalPoint(Pageable pageable, Long rentalPointId);

    List<Scooter> getAllScootersOfRentalPoint(Long rentalPointId);

    Scooter getScooterById(Long scooterId);

    void deleteScooterByID(Long scooterId);

    Scooter createNewScooter(Scooter scooter);

    Scooter saveScooter(Scooter scooter);

    Scooter setNotAvailableStatus(Long scooterId);

    Scooter setAvailableStatus(Long scooterId);
}

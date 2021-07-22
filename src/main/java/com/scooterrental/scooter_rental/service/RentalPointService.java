package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.dto.RentalPointDTO;

import java.util.List;
import java.util.Optional;

public interface RentalPointService {

    List<RentalPoint> findAllRentalPointsByCityId(Long cityId);

    Integer countRentalPointByCityId(Long cityId);

    RentalPoint getRentalPointById(Long rentalPointId);

    RentalPoint saveRentalPoint(RentalPoint rentalPoint);

    void deleteRentalPointById(Long rentalPointId);
}

package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.RentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;

import java.util.List;

public interface RentHistoryService {

    Page<RentHistory> getAllRentHistoriesByRentalPoint(Pageable pageable, Long rentalPointId);

    Page<RentHistory> getAllRentHistoriesByUsername(Pageable pageable, String username);

    Page<RentHistory> getAllRentHistoriesByScooterId(Pageable pageable, Long scooterId);

    Page<RentHistory> getAllRentHistories(Pageable pageable);

    RentHistory getRentHistoryById(Long rentHistoryId);

    RentHistory saveRentHistory(RentHistory rentHistory);

    void deleteRentHistoryById(Long rentHistoryId);

    RentHistory rentScooter(Long rentalPointId, Long scooterId, String period, String username);

    Catalog getRegionOfRentHistory(RentHistory rentHistory);

    Catalog getCountryOfRentHistory(RentHistory rentHistory);
}

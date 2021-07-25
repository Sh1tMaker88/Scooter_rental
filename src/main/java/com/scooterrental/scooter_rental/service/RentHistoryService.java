package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.model.RentHistory;

import java.util.List;

public interface RentHistoryService {

    List<RentHistory> getAllRentHistoriesByRentalPoint(Long rentalPointId);

//    List<RentHistory> getAllRentHistoriesByUsername(String username);

    List<RentHistory> getAllRentHistoriesByScooterId(Long scooterId);

    RentHistory getRentHistoryById(Long rentHistoryId);

    RentHistory saveRentHistory(RentHistory rentHistory);

    void deleteRentHistoryById(Long rentHistoryId);
}

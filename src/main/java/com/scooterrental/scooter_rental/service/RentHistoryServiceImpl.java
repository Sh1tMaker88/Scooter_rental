package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.repository.RentHistoryRepository;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import com.scooterrental.scooter_rental.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RentHistoryServiceImpl implements RentHistoryService{

    private final RentHistoryRepository rentHistoryRepository;
    private final RentalPointRepository rentalPointRepository;
    private final UserRepository userRepository;
    private final ScooterRepository scooterRepository;

    public RentHistoryServiceImpl(RentHistoryRepository rentHistoryRepository,
                                  RentalPointRepository rentalPointRepository,
                                  UserRepository userRepository, ScooterRepository scooterRepository) {
        this.rentHistoryRepository = rentHistoryRepository;
        this.rentalPointRepository = rentalPointRepository;
        this.userRepository = userRepository;
        this.scooterRepository = scooterRepository;
    }


    @Override
    public List<RentHistory> getAllRentHistoriesByRentalPoint(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN getAllRentHistoriesByRentalPoint - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        List<RentHistory> rentHistoryList = rentHistoryRepository.findAllRentHistoryByRentalPointId(rentalPointId);
        log.info("IN getAllRentHistoriesByRentalPoint - {} rent histories were found for rental point with ID={}",
                rentHistoryList.size(), rentalPointId);
        return rentHistoryList;
    }

//    @Override
//    public List<RentHistory> getAllRentHistoriesByUsername(String username) {
//        if (userRepository.findByUsername(username).isEmpty()) {
//            log.warn("IN getAllRentHistoriesByUsername - no such user with username={}", username);
//        }
//        List<RentHistory> rentHistoryList = rentHistoryRepository.findAllByUsername(username);
//        log.info("IN getAllRentHistoriesByUserId - {} rent histories were found for user with username={}",
//                rentHistoryList.size(), username);
//        return rentHistoryList;
//    }

    @Override
    public List<RentHistory> getAllRentHistoriesByScooterId(Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN getAllRentHistoriesByScooterId - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }
        List<RentHistory> rentHistoryList = rentHistoryRepository.findAllByScooterId(scooterId);
        log.info("IN getAllRentHistoriesByScooterId - {} rent histories were found for scooter with ID={}",
                rentHistoryList.size(), scooterId);
        return rentHistoryList;
    }

    @Override
    public RentHistory getRentHistoryById(Long rentHistoryId) {
        if (rentHistoryRepository.getRentHistoryById(rentHistoryId).isEmpty()) {
            log.warn("IN getRentHistoryById - no such rent history with ID={}", rentHistoryId);
            throw new ServiceException("No such rent history with ID=" + rentHistoryId);
        }
        RentHistory rentHistory = rentHistoryRepository.getRentHistoryById(rentHistoryId).get();
        log.info("IN getRentHistoryById - rental history with ID={} successfully found", rentHistoryId);
        return rentHistory;
    }

    @Override
    public RentHistory saveRentHistory(RentHistory rentHistory) {
        RentHistory savedRentHistory = rentHistoryRepository.save(rentHistory);
        log.info("IN saveRentHistory - rent history was saved");
        return savedRentHistory;
    }

    @Override
    public void deleteRentHistoryById(Long rentHistoryId) {
        if (rentHistoryRepository.getRentHistoryById(rentHistoryId).isEmpty()) {
            log.warn("IN deleteRentHistoryById - no such rent history with ID={}", rentHistoryId);
            throw new ServiceException("No such rent history with ID=" + rentHistoryId);
        }
        rentHistoryRepository.deleteById(rentHistoryId);
        log.info("IN deleteRentHistoryById - rent history with ID={} successfully deleted", rentHistoryId);
    }
}

package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.*;
import com.scooterrental.scooter_rental.repository.CatalogRepository;
import com.scooterrental.scooter_rental.repository.RentHistoryRepository;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@Slf4j
public class RentHistoryServiceImpl implements RentHistoryService{

    private final RentHistoryRepository rentHistoryRepository;
    private final RentalPointRepository rentalPointRepository;
    private final UserRepository userRepository;
    private final CatalogRepository catalogRepository;
    private final ScooterRepository scooterRepository;

    public RentHistoryServiceImpl(RentHistoryRepository rentHistoryRepository,
                                  RentalPointRepository rentalPointRepository,
                                  UserRepository userRepository, CatalogRepository catalogRepository,
                                  ScooterRepository scooterRepository) {
        this.rentHistoryRepository = rentHistoryRepository;
        this.rentalPointRepository = rentalPointRepository;
        this.userRepository = userRepository;
        this.catalogRepository = catalogRepository;
        this.scooterRepository = scooterRepository;
    }


    @Override
    public Page<RentHistory> getAllRentHistoriesByRentalPoint(Pageable pageable, Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN getAllRentHistoriesByRentalPoint - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        RentalPoint rentalPoint = rentalPointRepository.getRentalPointById(rentalPointId).get();
        Page<RentHistory> rentHistoryList = rentHistoryRepository.findAllRentHistoryByRentalPointId(pageable, rentalPoint);
        log.info("IN getAllRentHistoriesByRentalPoint - {} rent histories were found for rental point with ID={}",
                rentHistoryList.getTotalElements(), rentalPointId);
        return rentHistoryList;
    }

    @Override
    public Page<RentHistory> getAllRentHistoriesByUsername(Pageable pageable, String username) {
        if (userRepository.findByUsername(username).isEmpty()) {
            log.warn("IN getAllRentHistoriesByUsername - no such user with username={}", username);
        }
        User user = userRepository.findByUsername(username).get();
        Page<RentHistory> rentHistoryList = rentHistoryRepository.findAllByUserId(pageable, user);
        log.info("IN getAllRentHistoriesByUserId - {} rent histories were found for user with username={}",
                rentHistoryList.getTotalElements(), username);
        return rentHistoryList;
    }

    @Override
    public Page<RentHistory> getAllRentHistoriesByScooterId(Pageable pageable, Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN getAllRentHistoriesByScooterId - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }
        Scooter scooter = scooterRepository.getScooterById(scooterId).get();
        Page<RentHistory> rentHistoryList = rentHistoryRepository.findAllByScooterId(pageable, scooter);
        log.info("IN getAllRentHistoriesByScooterId - {} rent histories were found for scooter with ID={}",
                rentHistoryList.getTotalElements(), scooterId);
        return rentHistoryList;
    }

    @Override
    public Page<RentHistory> getAllRentHistories(Pageable pageable) {
        Page<RentHistory> rentHistoryList = rentHistoryRepository.findAll(pageable);
        log.info("IN getAllRentHistories - find {} rent orders", rentHistoryList.getTotalElements());
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

    @Override
    public RentHistory rentScooter(Long rentalPointId, Long scooterId, String period, String username) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN rentScooter - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }
        Scooter scooter = scooterRepository.getScooterById(scooterId).get();
        if (scooter.getStatus() != ScooterStatus.AVAILABLE) {
            log.warn("IN rentScooter - you cant rent scooter with ID={} because it's {}",
                    scooterId, scooter.getStatus());
            throw new ServiceException("Scooter have status=" + scooter.getStatus());
        }
        scooter.setStatus(ScooterStatus.OCCUPIED);

        if (userRepository.findByUsername(username).isEmpty()) {
            log.warn("IN rentScooter - no such user with username={}", username);
            throw new ServiceException("No such user with username=" + username);
        }
        User user = userRepository.findByUsername(username).get();

        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN rentScooter - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        RentalPoint rentalPoint = rentalPointRepository.getRentalPointById(rentalPointId).get();
        Double price = getPriceForRent(period, scooter);
        payTheRent(rentalPoint, price);

        RentHistory rentHistory = new RentHistory(period, price, user, scooter, rentalPoint);
        return rentHistoryRepository.save(rentHistory);
    }

    @Override
    public Catalog getRegionOfRentHistory(RentHistory rentHistory) {
        Long regionId = rentHistory.getRentalPointId().getCity().getParentId();
        if (catalogRepository.findById(regionId).isEmpty()) {
            log.warn("IN getRegionOfRentHistory - can not find any catalog item with ID={}", regionId);
            throw new ServiceException("Can not find any catalog item with ID=" + regionId);
        }
        Catalog region = catalogRepository.findById(regionId).get();
        log.info("IN getRegionOfRentHistory - found region with title={}", region.getTitle());
        return region;
    }

    @Override
    public Catalog getCountryOfRentHistory(RentHistory rentHistory) {
        Catalog region = getRegionOfRentHistory(rentHistory);
        if (catalogRepository.findById(region.getParentId()).isEmpty()) {
            log.warn("IN getCountryOfRentHistory - no such country with ID={}", region.getParentId());
            throw new ServiceException("No such country with ID=" + region.getParentId());
        }
        Catalog country = catalogRepository.findById(region.getParentId()).get();
        log.info("IN getCountryOfRentHistory - found country with title={}", region.getTitle());
        return country;
    }

    @Transactional(propagation = Propagation.NESTED)
    public void payTheRent(RentalPoint rentalPoint, Double price) {
        rentalPoint.setBalance(rentalPoint.getBalance() + price);
        rentalPointRepository.save(rentalPoint);
    }

    private Double getPriceForRent(String period, Scooter scooter) {
        switch (period) {
            case "one_hour":
                return scooter.getRentalPrice().getOneHour();
            case "two_hours":
                return scooter.getRentalPrice().getTwoHours();
            case "three_hours":
                return scooter.getRentalPrice().getThreeHours();
            case "one_day":
                return scooter.getRentalPrice().getOneDay();
            case "two_days":
                return scooter.getRentalPrice().getTwoDays();
            case "week":
                return scooter.getRentalPrice().getWeek();
            case "month":
                return scooter.getRentalPrice().getMonth();
            default:
                log.warn("IN getPriceForRent - no such period for rent: {}", period);
                throw new ServiceException("No such period for rent: " + period);
        }
    }
}

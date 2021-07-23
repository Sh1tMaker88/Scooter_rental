package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.repository.RentalPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class RentalPriceServiceImpl implements RentalPriceService {

    private final RentalPriceRepository rentalPriceRepository;
    private final ScooterService scooterService;

    public RentalPriceServiceImpl(RentalPriceRepository rentalPriceRepository, ScooterService scooterService) {
        this.rentalPriceRepository = rentalPriceRepository;
        this.scooterService = scooterService;
    }

    @Override
    public RentalPrice getRentalPriceById(Long rentalPriceId) {
        if (rentalPriceRepository.getRentalPriceById(rentalPriceId).isEmpty()) {
            log.warn("IN getRentalPriceById - no such rental_price with ID={}", rentalPriceId);
            throw new ServiceException("No such rental price with ID=" + rentalPriceId);
        }
        RentalPrice rentalPrice = rentalPriceRepository.getRentalPriceById(rentalPriceId).get();
        log.info("IN getRentalPriceById - rental_price with ID={} successfully loaded", rentalPriceId);
        return rentalPrice;
    }

    @Override
    public List<RentalPrice> findAllRentalPrice() {
        List<RentalPrice> priceList = rentalPriceRepository.findAll();
        log.info("IN findAllRentalPrice - {} price sets was found", priceList.size());
        return priceList;
    }

    @Override
    public void deleteRentalPriceById(Long rentalPriceId) {
        if (rentalPriceRepository.getRentalPriceById(rentalPriceId).isEmpty()) {
            log.warn("IN deleteRentalPriceById - no such price set with ID={}", rentalPriceId);
            throw new ServiceException("No price set with ID=" + rentalPriceId);
        }
        rentalPriceRepository.deleteRentalPriceById(rentalPriceId);
        log.info("IN deleteRentalPriceById - price set with ID={} successfully deleted", rentalPriceId);
    }

    @Override
    public RentalPrice saveRentalPrice(RentalPrice rentalPrice) {
        RentalPrice priceSet = rentalPriceRepository.save(rentalPrice);
        log.info("IN saveRentalPrice - price set with title:'{}' was saved", rentalPrice.getTitle());
        return priceSet;
    }

    @Override
    public Scooter setNewRentalPriceToScooter(RentalPrice rentalPrice, Long scooterId) {
        Scooter scooter = scooterService.getScooterById(scooterId);
        RentalPrice savedPrice = saveRentalPrice(rentalPrice);
        scooter.setRentalPrice(savedPrice);
        Scooter savedScooter = scooterService.saveScooter(scooter);
        log.info("IN setRentalPriceToScooter - successfully saved price set {} for scooter with ID={}",
                savedPrice.getTitle(), scooterId);
        return savedScooter;
    }

    @Override
    public Scooter setPriceForScooterByPriceId(Long priceId, Long scooterId) {
        Scooter scooter = scooterService.getScooterById(scooterId);
        scooter.setRentalPrice(getRentalPriceById(priceId));
        Scooter savedScooter = scooterService.saveScooter(scooter);
        log.info("IN setPriceForScooterByPriceId - successfully saved price set with ID={} for scooter with ID={}",
                priceId, scooterId);
        return savedScooter;
    }
}

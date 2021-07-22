package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.repository.RentalPriceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.serial.SerialException;
import java.util.List;

@Service
@Transactional
@Slf4j
public class RentalPriceServiceImpl implements RentalPriceService {

    private final RentalPriceRepository rentalPriceRepository;

    public RentalPriceServiceImpl(RentalPriceRepository rentalPriceRepository) {
        this.rentalPriceRepository = rentalPriceRepository;
    }

    @Override
    public RentalPrice getRentalPriceById(Long rentalPriceId) {
        if (rentalPriceRepository.getRentalPriceById(rentalPriceId).isEmpty()) {
            log.warn("IN getRentalPriceById - no such rental_price with ID={}", rentalPriceId);
            throw new ServiceException("No such rental price with ID=" + rentalPriceId);
        }
        log.info("IN getRentalPriceById - rental_price with ID={} successfully loaded", rentalPriceId);
        return rentalPriceRepository.getRentalPriceById(rentalPriceId).get();
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
}

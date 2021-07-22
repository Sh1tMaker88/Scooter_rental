package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
public class ScooterServiceImpl implements ScooterService {

    private final ScooterRepository scooterRepository;
    private final RentalPointRepository rentalPointRepository;

    public ScooterServiceImpl(ScooterRepository scooterRepository, RentalPointRepository rentalPointRepository) {
        this.scooterRepository = scooterRepository;
        this.rentalPointRepository = rentalPointRepository;
    }


    @Override
    public List<Scooter> getAllScooters(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }

        return scooterRepository.findAllByRentalPointId(rentalPointId);
    }

    @Override
    public Scooter getScooterById(Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }

        return scooterRepository.getScooterById(scooterId).get();
    }

    @Override
    public void deleteScooterByID(Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN deleteScooterByID - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }

        scooterRepository.deleteScooterById(scooterId);
        log.info("IN deleteScooterByID - scooter with ID={} successfully deleted", scooterId);
    }

    @Override
    public Scooter saveScooter(Scooter scooter) {

        return scooterRepository.save(scooter);
    }
}

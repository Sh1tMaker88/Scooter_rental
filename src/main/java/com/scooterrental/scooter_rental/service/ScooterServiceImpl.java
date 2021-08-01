package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.ScooterStatus;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import com.scooterrental.scooter_rental.repository.ScooterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ScooterServiceImpl implements ScooterService {

    private final ScooterRepository scooterRepository;
    private final RentalPointRepository rentalPointRepository;

    public ScooterServiceImpl(ScooterRepository scooterRepository, RentalPointRepository rentalPointRepository) {
        this.scooterRepository = scooterRepository;
        this.rentalPointRepository = rentalPointRepository;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Scooter> getAllScootersOfRentalPoint(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN getAllScooters - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        List<Scooter> scooterList = scooterRepository.findAllByRentalPointId(rentalPointId);
        log.info("IN getAllScooters - {} scooter were found", scooterList.size());
        return scooterList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Page<Scooter> getAllScootersOfRentalPoint(Pageable pageable, Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN getAllScooters - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        Page<Scooter> scooterList = scooterRepository.findAllByRentalPointId(pageable, rentalPointId);
        log.info("IN getAllScooters - {} scooter were found", scooterList.getTotalElements());
        return scooterList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Scooter getScooterById(Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN getScooterById - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }
        Scooter scooter = scooterRepository.getScooterById(scooterId).get();
        log.info("IN getScooterById - scooter with ID={} successfully loaded", scooterId);
        return scooter;
    }

    @Override
    @Transactional
    public void deleteScooterByID(Long scooterId) {
        if (scooterRepository.getScooterById(scooterId).isEmpty()) {
            log.warn("IN deleteScooterByID - no such scooter with ID={}", scooterId);
            throw new ServiceException("No such scooter with ID=" + scooterId);
        }
        scooterRepository.deleteScooterById(scooterId);
        log.info("IN deleteScooterByID - scooter with ID={} successfully deleted", scooterId);
    }

    @Override
    @Transactional
    public Scooter saveScooter(Scooter scooter) {
        Scooter savedScooter = scooterRepository.save(scooter);
        log.info("IN saveScooter - scooter with serial number {} successfully saved", savedScooter.getSerialNumber());
        return savedScooter;
    }

    @Override
    @Transactional
    public Scooter setNotAvailableStatus(Long scooterId) {
        Scooter scooter = getScooterById(scooterId);
        scooter.setStatus(ScooterStatus.NOT_AVAILABLE);
        return saveScooter(scooter);
    }

    @Override
    @Transactional
    public Scooter setAvailableStatus(Long scooterId) {
        Scooter scooter = getScooterById(scooterId);
        scooter.setStatus(ScooterStatus.AVAILABLE);
        return saveScooter(scooter);
    }
}

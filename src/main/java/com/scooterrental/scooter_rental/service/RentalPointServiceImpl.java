package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RentalPointServiceImpl implements RentalPointService {

    private final RentalPointRepository rentalPointRepository;

    public RentalPointServiceImpl(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<RentalPoint> findAllRentalPointsByCityId(Long cityId) {
        List<RentalPoint> rentalPointList = rentalPointRepository.findAllByCityId(cityId);
        log.info("IN findAllRentalPointsByCityId - found {} rental points", rentalPointList.size());
        return rentalPointList;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Integer countRentalPointByCityId(Long cityId) {
        Integer rentalPointByCityId = rentalPointRepository.countRentalPointByCityId(cityId);
        log.info("IN countRentalPointByCityId - in city with ID={} found {} rental points", cityId, rentalPointByCityId);
        return rentalPointByCityId;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public RentalPoint getRentalPointById(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN getRentalPointById - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        RentalPoint rentalPoint = rentalPointRepository.getRentalPointById(rentalPointId).get();
        log.info("IN getRentalPointById - found rental point with ID={}", rentalPointId);
        return rentalPoint;
    }

    @Override
    @Transactional
    public RentalPoint saveRentalPoint(RentalPoint rentalPoint) {
        RentalPoint savedRentalPoint = rentalPointRepository.save(rentalPoint);
        log.info("IN saveRentalPoint - rental point with title={} was saved", savedRentalPoint.getTitle());
        return savedRentalPoint;
    }

    @Override
    @Transactional
    public void deleteRentalPointById(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            log.warn("IN deleteRentalPointById - no such rental point with ID={}", rentalPointId);
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        rentalPointRepository.deleteRentalPointById(rentalPointId);
        log.info("IN deleteRentalPointById - rental point with ID={} was deleted", rentalPointId);
    }

}

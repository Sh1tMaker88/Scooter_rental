package com.scooterrental.scooter_rental.service;

import com.scooterrental.scooter_rental.exception.ServiceException;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.repository.RentalPointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalPointServiceImpl implements RentalPointService {

    private final RentalPointRepository rentalPointRepository;

    public RentalPointServiceImpl(RentalPointRepository rentalPointRepository) {
        this.rentalPointRepository = rentalPointRepository;
    }

    @Override
    public List<RentalPoint> findAllRentalPointsByCityId(Long cityId) {
        return rentalPointRepository.findAllByCityId(cityId);
    }

    @Override
    public Integer countRentalPointByCityId(Long cityId) {
        return rentalPointRepository.countRentalPointByCityId(cityId);
    }

    @Override
    public RentalPoint getRentalPointById(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }

        return rentalPointRepository.getRentalPointById(rentalPointId).get();
    }

    @Override
    public RentalPoint saveRentalPoint(RentalPoint rentalPoint) {

        return rentalPointRepository.save(rentalPoint);
    }

    @Override
    public void deleteRentalPointById(Long rentalPointId) {
        if (rentalPointRepository.getRentalPointById(rentalPointId).isEmpty()) {
            throw new ServiceException("No such rental point with ID=" + rentalPointId);
        }
        rentalPointRepository.deleteRentalPointById(rentalPointId);
    }

}

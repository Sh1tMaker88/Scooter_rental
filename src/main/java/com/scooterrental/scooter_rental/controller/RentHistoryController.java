package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.model.dto.RentHistoryDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.security.controller.UserController;
import com.scooterrental.scooter_rental.service.RentHistoryService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RentHistoryController {

    private final RentHistoryService rentHistoryService;
    private final MapStructMapper mapStructMapper;

    public RentHistoryController(RentHistoryService rentHistoryService, MapStructMapper mapStructMapper) {
        this.rentHistoryService = rentHistoryService;
        this.mapStructMapper = mapStructMapper;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/history")
    public ResponseEntity<CollectionModel<RentHistoryDTO>> getRentHistoryOfRentalPoint(@PathVariable String country,
                                                                                       @PathVariable String region,
                                                                                       @PathVariable String city,
                                                                                       @PathVariable Long rentalPointId) {
        List<RentHistory> rentHistoryList = rentHistoryService.getAllRentHistoriesByRentalPoint(rentalPointId);
        List<RentHistoryDTO> rentHistoryDTOS = mapStructMapper.toRentHistoryListDTO(rentHistoryList);
        for (RentHistoryDTO rentHistoryDTO : rentHistoryDTOS) {
            setLinksForRentHistoryRepresentation(country, region, city, rentalPointId, rentHistoryDTO);
        }

        return ResponseEntity.ok(CollectionModel.of(rentHistoryDTOS));
    }



    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}//scooters/{scooterId}/history")
    public ResponseEntity<CollectionModel<RentHistoryDTO>> getRentHistoryOfScooter(@PathVariable String country,
                                                                                   @PathVariable String region,
                                                                                   @PathVariable String city,
                                                                                   @PathVariable Long rentalPointId,
                                                                                   @PathVariable Long scooterId) {
        List<RentHistory> rentHistoryList = rentHistoryService.getAllRentHistoriesByScooterId(scooterId);
        List<RentHistoryDTO> rentHistoryDTOS = mapStructMapper.toRentHistoryListDTO(rentHistoryList);
        for (RentHistoryDTO rentHistoryDTO : rentHistoryDTOS) {
            setLinksForRentHistoryRepresentation(country, region, city, rentalPointId, rentHistoryDTO);
        }

        return ResponseEntity.ok(CollectionModel.of(rentHistoryDTOS));
    }


    private void setLinksForRentHistoryRepresentation(String country, String region, String city,
                                                      Long rentalPointId, RentHistoryDTO rentHistoryDTO) {
        rentHistoryDTO.getRentalPointId().add(linkTo(methodOn(RentalPointController.class)
                .getRentalPointRepresentation(country, region, city, rentalPointId))
                .withSelfRel()
                .withType("GET"));
        rentHistoryDTO.getScooterId().add(linkTo(methodOn(ScooterController.class).getScooter(country, region,
                city, rentalPointId, rentHistoryDTO.getScooterId().getId()))
                .withSelfRel()
                .withType("GET"));
        rentHistoryDTO.getUserId().add(linkTo(methodOn(UserController.class)
                .getUserById(rentHistoryDTO.getUserId().getId()))
                .withSelfRel()
                .withType("GET"));
    }
}

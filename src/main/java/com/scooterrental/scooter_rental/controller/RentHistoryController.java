package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.dto.RentHistoryDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.security.controller.UserController;
import com.scooterrental.scooter_rental.service.RentHistoryService;
import com.scooterrental.scooter_rental.service.ScooterService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RentHistoryController {

    private final RentHistoryService rentHistoryService;
    private final ScooterService scooterService;
    private final MapStructMapper mapStructMapper;

    public RentHistoryController(RentHistoryService rentHistoryService, ScooterService scooterService, MapStructMapper mapStructMapper) {
        this.rentHistoryService = rentHistoryService;
        this.scooterService = scooterService;
        this.mapStructMapper = mapStructMapper;
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/history")
    public ResponseEntity<CollectionModel<RentHistoryDTO>> getRentHistoryOfScooter(@PathVariable String country,
                                                                                   @PathVariable String region,
                                                                                   @PathVariable String city,
                                                                                   @PathVariable Long rentalPointId,
                                                                                   @PathVariable Long scooterId) {
        List<RentHistory> rentHistoryList = rentHistoryService.getAllRentHistoriesByScooterId(scooterId);
        List<RentHistoryDTO> rentHistoryDTOS = mapStructMapper.toRentHistoryListDTO(rentHistoryList);
        for (RentHistoryDTO rentHistoryDTO : rentHistoryDTOS) {
            ControllerUtil.setLinksForRentHistoryRepresentation(country, region, city, rentalPointId, rentHistoryDTO);
        }

        return ResponseEntity.ok(CollectionModel.of(rentHistoryDTOS));
    }

    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/rent")
    public ResponseEntity<EntityModel<RentalPrice>> getVariationsToRentScooter(@PathVariable String country,
                                                                               @PathVariable String region,
                                                                               @PathVariable String city,
                                                                               @PathVariable Long rentalPointId,
                                                                               @PathVariable Long scooterId) {
        RentalPrice rentalPrice = scooterService.getScooterById(scooterId).getRentalPrice();
        List<Link> links = ControllerUtil.getLinksToRent(country, region, city, rentalPointId, scooterId);
        return ResponseEntity.ok(EntityModel.of(rentalPrice, links));
    }

    @PostMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/rent/{period}")
    public ResponseEntity<RentHistory> rentScooter(@PathVariable String country,
                                                   @PathVariable String region,
                                                   @PathVariable String city,
                                                   @PathVariable Long rentalPointId,
                                                   @PathVariable Long scooterId,
                                                   @PathVariable String period) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        RentHistory rent = rentHistoryService.rentScooter(rentalPointId, scooterId, period, username);
        return ResponseEntity.ok(rent);
    }

//    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/rent/{rentId}")
//    public ResponseEntity<RentHistory> rentScooter(@PathVariable String country,
//                                                   @PathVariable String region,
//                                                   @PathVariable String city,
//                                                   @PathVariable Long rentalPointId,
//                                                   @PathVariable Long scooterId,
//                                                   @PathVariable Long rentId) {
//        //todo check is user have permissions to see this rentID
//    }
//
//    @PutMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/rent/{rentId}")
//    public ResponseEntity<RentHistory> rentScooter(@PathVariable String country,
//                                                   @PathVariable String region,
//                                                   @PathVariable String city,
//                                                   @PathVariable Long rentalPointId,
//                                                   @PathVariable Long scooterId,
//                                                   @PathVariable Long rentId) {
//
//    }
}

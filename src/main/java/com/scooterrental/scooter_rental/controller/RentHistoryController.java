package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.dto.RentHistoryDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.service.RentHistoryService;
import com.scooterrental.scooter_rental.service.ScooterService;
import com.scooterrental.scooter_rental.util.ControllerUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PagedModel<EntityModel<RentHistory>>> getRentHistoryOfScooter(
            @PathVariable String country,
            @PathVariable String region,
            @PathVariable String city,
            @PathVariable Long rentalPointId,
            @PathVariable Long scooterId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id, asc") List<String> sort,
            PagedResourcesAssembler<RentHistory> assembler) {
        PageRequest pageRequest = ControllerUtil.getPageRequestWithPaginationAndSort(page, pageSize, sort);
        Page<RentHistory> rentHistoryPage = rentHistoryService.getAllRentHistoriesByScooterId(pageRequest, scooterId);
        for (RentHistory rentHistory : rentHistoryPage.getContent()) {
            rentHistory.add(linkTo(methodOn(RentHistoryController.class)
                    .getRentHistoryById(rentHistory.getId()))
                    .withSelfRel());
        }
        return ResponseEntity.ok(assembler.toModel(rentHistoryPage));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/history")
    public ResponseEntity<PagedModel<EntityModel<RentHistory>>> getRentHistoryOfRentalPoint(
            @PathVariable String country,
            @PathVariable String region,
            @PathVariable String city,
            @PathVariable Long rentalPointId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id, asc") List<String> sort,
            PagedResourcesAssembler<RentHistory> assembler) {
        PageRequest pageRequest = ControllerUtil.getPageRequestWithPaginationAndSort(page, pageSize, sort);
        Page<RentHistory> rentHistoryPage = rentHistoryService.getAllRentHistoriesByRentalPoint(pageRequest, rentalPointId);
        for (RentHistory rentHistory : rentHistoryPage.getContent()) {
            rentHistory.add(linkTo(methodOn(RentHistoryController.class)
                    .getRentHistoryById(rentHistory.getId()))
                    .withSelfRel());
        }
        return ResponseEntity.ok(assembler.toModel(rentHistoryPage));
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rent_history")
    public ResponseEntity<PagedModel<EntityModel<RentHistory>>> getAllHistory(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id, asc") List<String> sort,
            PagedResourcesAssembler<RentHistory> assembler) {
        PageRequest pageRequest = ControllerUtil.getPageRequestWithPaginationAndSort(page, pageSize, sort);
        Page<RentHistory> rentHistoryPage = rentHistoryService.getAllRentHistories(pageRequest);
        for (RentHistory rentHistory : rentHistoryPage.getContent()) {
            rentHistory.add(linkTo(methodOn(RentHistoryController.class)
                    .getRentHistoryById(rentHistory.getId()))
                    .withSelfRel());
        }
        return ResponseEntity.ok(assembler.toModel(rentHistoryPage));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/rent_history/{rentHistoryId}")
    public ResponseEntity<RentHistoryDTO> getRentHistoryById(@PathVariable Long rentHistoryId) {
        RentHistory rentHistory = rentHistoryService.getRentHistoryById(rentHistoryId);
        String country = rentHistoryService.getCountryOfRentHistory(rentHistory).getTitle();
        String region = rentHistoryService.getRegionOfRentHistory(rentHistory).getTitle();
        String city = rentHistory.getRentalPointId().getCity().getTitle();
        RentHistoryDTO rentHistoryDTO = mapStructMapper.toRentHistoryDTO(rentHistory);

        ControllerUtil.setLinksForRentHistoryRepresentation(country, region, city,
                rentHistory.getRentalPointId().getId(), rentHistoryDTO);

        return ResponseEntity.ok(rentHistoryDTO);
    }


//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/rent/{rentId}")
//    public ResponseEntity<RentHistory> rentScooter(@PathVariable String country,
//                                                   @PathVariable String region,
//                                                   @PathVariable String city,
//                                                   @PathVariable Long rentalPointId,
//                                                   @PathVariable Long scooterId,
//                                                   @PathVariable Long rentId) {
//
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

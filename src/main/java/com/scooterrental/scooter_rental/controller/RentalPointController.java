package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.dto.RentalPointDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.service.CatalogService;
import com.scooterrental.scooter_rental.service.RentalPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/rental_points")
public class RentalPointController {

    private final RentalPointService rentalPointService;
    private final CatalogService catalogService;
    private final MapStructMapper mapStructMapper;

    public RentalPointController(RentalPointService rentalPointService, CatalogService catalogService,
                                 MapStructMapper mapStructMapper) {
        this.rentalPointService = rentalPointService;
        this.catalogService = catalogService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{country}/{region}/{city}")
    public ResponseEntity<CollectionModel<RentalPointDTO>> getAllCityRentalPoints(@PathVariable String country,
                                                                                  @PathVariable String region,
                                                                                  @PathVariable String city) {
        String cityStr = city.substring(0, 1).toUpperCase() + city.substring(1);
        Long cityId = catalogService.getCatalogByTitle(cityStr).getId();

        List<RentalPoint> rentalPoints = rentalPointService.findAllRentalPointsByCityId(cityId);
        List<RentalPointDTO> rentalPointDTOList = mapStructMapper.toRentalPointsDTOList(rentalPoints);
        for (RentalPointDTO rentalPointDTO : rentalPointDTOList) {
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/" + country + "/" + region + "/" + city)
                    .slash(rentalPointDTO.getId())
                    .withSelfRel();
            rentalPointDTO.add(catalogItem);
        }

        Link linkToCreate = linkTo(RentalPointController.class)
                .slash("/" + country + "/" + region + "/" + city)
                .withRel("add_new_rental_point_to_this_city")
                .withType("POST");
        Link linkToUpdate = linkTo(RentalPointController.class)
                .slash("/" + country + "/" + region + "/" + city)
                .withRel("edit_this_city")
                .withType("PUT");
        Link linkToDelete = linkTo(methodOn(CatalogController.class)
                .deleteCity(country, region, city))
                .withRel("delete_this_city")
                .withType("DELETE");

        return ResponseEntity.ok(CollectionModel.of(rentalPointDTOList, linkToCreate, linkToUpdate, linkToDelete));
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}")
    public ResponseEntity<EntityModel<RentalPointDTO>> getRentalPointRepresentation(@PathVariable String country,
                                                                                   @PathVariable String region,
                                                                                   @PathVariable String city,
                                                                                   @PathVariable Long rentalPointId) {
        RentalPoint rentalPoint = rentalPointService.getRentalPointById(rentalPointId);
        int scootersOfRentalPoint = rentalPoint.getScooterList().size();
        RentalPointDTO pointDTO = mapStructMapper.toRentalPointDTO(rentalPoint);
        Link linkToGet = linkTo(methodOn(ScooterController.class)
                .getAllScootersOfRentalPoint(country, region, city, rentalPointId))
                .withRel("scooters_of_rental_point(total: " + scootersOfRentalPoint + ")")
                .withType("GET");
        Link linkRentHistory = linkTo(methodOn(RentHistoryController.class)
                .getRentHistoryOfRentalPoint(country, region, city, rentalPointId))
                .withRel("history_of_this_rental_point")
                .withTitle("GET");
        Link linkToCreate = linkTo(RentalPointController.class)
                .slash("/" + country + "/" + "/" + region + "/" + city + "/" + rentalPointId + "/scooters")
                .withRel("add_new_scooter")
                .withType("POST");
        Link linkToUpdate = linkTo(RentalPointController.class)
                .slash("/" + country + "/" + "/" + region + "/" + city + "/" + rentalPointId)
                .withRel("edit_this_rental_point")
                .withType("PUT");
        Link linkToDelete = linkTo(methodOn(RentalPointController.class)
                .deleteRentalPoint(country, region, city, rentalPointId))
                .withRel("delete_rental_point")
                .withType("DELETE");

        return ResponseEntity.ok(EntityModel.of(pointDTO, linkToGet, linkRentHistory,
                linkToCreate, linkToUpdate, linkToDelete));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{country}/{region}/{city}")
    public ResponseEntity<RentalPointDTO> addRentalPoint(@PathVariable String country,
                                                         @PathVariable String region,
                                                         @PathVariable String city,
                                                         @RequestBody RentalPointDTO rentalPointDTO) {
        RentalPoint rentalPoint = rentalPointService.saveRentalPoint(mapStructMapper.toRentalPoint(rentalPointDTO));

        return ResponseEntity.ok(mapStructMapper.toRentalPointDTO(rentalPoint));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{country}/{region}/{city}/{rentalPointId}")
    public ResponseEntity<RentalPointDTO> updateRentalPoint(@PathVariable String country,
                                                            @PathVariable String region,
                                                            @PathVariable String city,
                                                            @PathVariable Long rentalPointId,
                                                            @RequestBody RentalPointDTO rentalPointDTO) {
        rentalPointDTO.setId(rentalPointId);
        RentalPoint savedRentalPoint = rentalPointService.saveRentalPoint(mapStructMapper.toRentalPoint(rentalPointDTO));
        return ResponseEntity.ok(mapStructMapper.toRentalPointDTO(savedRentalPoint));
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{country}/{region}/{city}/{rentalPointId}")
    public ResponseEntity<RentalPoint> deleteRentalPoint(@PathVariable String country,
                                            @PathVariable String region,
                                            @PathVariable String city,
                                            @PathVariable Long rentalPointId) {
        rentalPointService.deleteRentalPointById(rentalPointId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

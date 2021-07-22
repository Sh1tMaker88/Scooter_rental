package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.dto.RentalPointDTO;
import com.scooterrental.scooter_rental.model.dto.RentalPointWithScooterDTO;
import com.scooterrental.scooter_rental.model.dto.ScooterDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.service.CatalogService;
import com.scooterrental.scooter_rental.service.RentalPointService;
import lombok.extern.slf4j.Slf4j;
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

    public RentalPointController(RentalPointService rentalPointService, CatalogService catalogService, MapStructMapper mapStructMapper) {
        this.rentalPointService = rentalPointService;
        this.catalogService = catalogService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{country}/{region}/{city}")
    public ResponseEntity<List<RentalPointDTO>> getAllCityRentalPoints(@PathVariable String country,
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

        return ResponseEntity.ok(rentalPointDTOList);
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}")
    public ResponseEntity<RentalPointWithScooterDTO> getRentalPointRepresentation(@PathVariable String country,
                                                                                  @PathVariable String region,
                                                                                  @PathVariable String city,
                                                                                  @PathVariable Long rentalPointId) {
        RentalPoint rentalPoint = rentalPointService.getRentalPointById(rentalPointId);
        RentalPointWithScooterDTO pointDTO = mapStructMapper.toRentalPointWithScooterDTO(rentalPoint);
        pointDTO.add(linkTo(methodOn(RentalPointController.class)
                .deleteRentalPoint(country, region, city, rentalPointId))
                .withRel("delete_rental_point")
                .withType("DELETE"));
        pointDTO.add(linkTo(methodOn(ScooterController.class)
                .getAllScootersOfRentalPoint(country, region, city, rentalPointId))
                .withRel("scooters_of_rental_point")
                .withType("GET"));
        //todo add link to add scooter

        return ResponseEntity.ok(pointDTO);
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
    @DeleteMapping("/{country}/{region}/{city}/{rentalPointId}")
    public ResponseEntity<Long> deleteRentalPoint(@PathVariable String country,
                                            @PathVariable String region,
                                            @PathVariable String city,
                                            @PathVariable Long rentalPointId) {
        rentalPointService.deleteRentalPointById(rentalPointId);

        return new ResponseEntity<>(rentalPointId, HttpStatus.OK);
    }
//
//    @GetMapping("belarus/minsk_region/slutsk")
//    public ResponseEntity<List<Catalog>> getSlutskChildren() {
//        List<Catalog> catalogList = catalogService.findAllSecondLevelTree("Slutsk");
//        for (Catalog catalog : catalogList) {
//            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
//            Link catalogItem = linkTo(StarterController.class)
//                    .slash("/rental_points/belarus/minsk_region/slutsk")
//                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
//                    .withRel("Elements in section: " + childrenOfItem);
//            catalog.add(catalogItem);
//        }
//
//        return ResponseEntity.ok(catalogList);
//    }
}

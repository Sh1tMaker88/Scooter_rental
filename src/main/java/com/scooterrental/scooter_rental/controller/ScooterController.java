package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.dto.ScooterDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.service.RentalPointService;
import com.scooterrental.scooter_rental.service.ScooterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/rental_points")
public class ScooterController {

    private final ScooterService scooterService;
    private final RentalPointService rentalPointService;
    private final MapStructMapper mapStructMapper;

    public ScooterController(ScooterService scooterService, RentalPointService rentalPointService, MapStructMapper mapStructMapper) {
        this.scooterService = scooterService;
        this.rentalPointService = rentalPointService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}/scooters")
    public ResponseEntity<List<ScooterDTO>> getAllScootersOfRentalPoint(@PathVariable String country,
                                                                        @PathVariable String region,
                                                                        @PathVariable String city,
                                                                        @PathVariable Long rentalPointId) {
        List<Scooter> scooterList = scooterService.getAllScooters(rentalPointId);
        List<ScooterDTO> scooterDTOList = mapStructMapper.toScooterDTOList(scooterList);
        for (ScooterDTO scooterDTO : scooterDTOList) {
            scooterDTO.add(linkTo(ScooterController.class)
                    .slash("/" + country + "/" + region + "/" + city +
                            "/" + rentalPointId + "/scooters" + "/" + scooterDTO.getId())
                    .withSelfRel());
            scooterDTO.add(linkTo(methodOn(RentalPointController.class)
                    .getRentalPointRepresentation(country, region, city, rentalPointId))
                    .withRel("to_rental_point"));
        }
        //todo add links for delete scooter and get one

        return ResponseEntity.ok(scooterDTOList);
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}")
    public ResponseEntity<Scooter> getScooter(@PathVariable String country,
                                              @PathVariable String region,
                                              @PathVariable String city,
                                              @PathVariable Long rentalPointId,
                                              @PathVariable Long scooterId) {
        Scooter scooter = scooterService.getScooterById(scooterId);
        scooter.getRentalPrice().add(linkTo(methodOn(RentalPriceController.class)
                .getRentalPriceOfScooter(country, region, city, rentalPointId, scooterId,
                        scooter.getRentalPrice().getId()))
                .withSelfRel()
                .withType("GET"));
        //todo link to update
//        scooter.add(linkTo())
        scooter.add(linkTo(methodOn(ScooterController.class)
                .deleteScooter(country, region, city, rentalPointId, scooterId))
                .withRel("delete_scooter")
                .withType("DELETE"));
        scooter.add(linkTo(methodOn(RentalPointController.class)
                .getRentalPointRepresentation(country, region, city, rentalPointId))
                .withRel("to_rental_point")
                .withType("GET"));




        return ResponseEntity.ok(scooter);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/{country}/{region}/{city}/{rentalPointId}/scooters")
    public ResponseEntity<Scooter> addScooter(@PathVariable String country,
                                              @PathVariable String region,
                                              @PathVariable String city,
                                              @PathVariable Long rentalPointId,
                                              @RequestBody Scooter scooter) {
        scooter.setRentalPoint(rentalPointService.getRentalPointById(rentalPointId));
        Scooter scooterFromDB = scooterService.saveScooter(scooter);
        return ResponseEntity.status(HttpStatus.CREATED).body(scooterFromDB);
    }

    //todo add drop rental price endpoint

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}")
    public ResponseEntity<Scooter> updateScooter(@PathVariable String country,
                                                 @PathVariable String region,
                                                 @PathVariable String city,
                                                 @PathVariable Long rentalPointId,
                                                 @PathVariable Long scooterId,
                                                 @RequestBody Scooter scooter) {
        scooter.setRentalPoint(rentalPointService.getRentalPointById(rentalPointId));
        scooter.setId(scooterId);
        Scooter scooterFromDB = scooterService.saveScooter(scooter);

        return ResponseEntity.status(HttpStatus.OK).body(scooterFromDB);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}")
    public ResponseEntity<Scooter> deleteScooter(@PathVariable String country,
                                                 @PathVariable String region,
                                                 @PathVariable String city,
                                                 @PathVariable Long rentalPointId,
                                                 @PathVariable Long scooterId) {
        scooterService.deleteScooterByID(scooterId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }


}

package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Scooter;
import com.scooterrental.scooter_rental.model.dto.ScooterDTO;
import com.scooterrental.scooter_rental.model.dto.mapper.MapStructMapper;
import com.scooterrental.scooter_rental.service.RentalPointService;
import com.scooterrental.scooter_rental.service.ScooterService;
import com.scooterrental.scooter_rental.util.ControllerUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
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
@RequestMapping("/rental_points")
public class ScooterController {

    private final ScooterService scooterService;
    private final RentalPointService rentalPointService;
    private final MapStructMapper mapStructMapper;

    public ScooterController(ScooterService scooterService, RentalPointService rentalPointService,
                             MapStructMapper mapStructMapper) {
        this.scooterService = scooterService;
        this.rentalPointService = rentalPointService;
        this.mapStructMapper = mapStructMapper;
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}/scooters")
    public ResponseEntity<CollectionModel<EntityModel<ScooterDTO>>> getAllScootersOfRentalPoint(
            @PathVariable String country,
            @PathVariable String region,
            @PathVariable String city,
            @PathVariable Long rentalPointId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id, asc") List<String> sort,
            PagedResourcesAssembler<ScooterDTO> assembler) {
        PageRequest pageRequest = ControllerUtil.getPageRequestWithPaginationAndSort(page, pageSize, sort);
        List<ScooterDTO> scooterDTOList = mapStructMapper.toScooterDTOList(
                scooterService.getAllScootersOfRentalPoint(rentalPointId));
        Page<ScooterDTO> scooterDTOPage = new PageImpl<>(scooterDTOList, pageRequest, scooterDTOList.size());
        for (ScooterDTO scooterDTO : scooterDTOPage) {
            scooterDTO.add(linkTo(ScooterController.class)
                    .slash("/" + country + "/" + region + "/" + city +
                            "/" + rentalPointId + "/scooters" + "/" + scooterDTO.getId())
                    .withSelfRel());
        }
        Link linkToCreate = linkTo(RentalPointController.class)
                .slash("/" + country + "/" + "/" + region + "/" + city + "/" + rentalPointId + "/scooters")
                .withRel("add_new_scooter_to_this_rental_point")
                .withType("POST");
        Link link = linkTo(methodOn(RentalPointController.class)
                .getRentalPointRepresentation(country, region, city, rentalPointId))
                .withRel("to_rental_point")
                .withType("GET");

        return ResponseEntity.ok(assembler.toModel(scooterDTOPage).add(linkToCreate, link));
    }

    @GetMapping("/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}")
    public ResponseEntity<Scooter> getScooter(@PathVariable String country,
                                              @PathVariable String region,
                                              @PathVariable String city,
                                              @PathVariable Long rentalPointId,
                                              @PathVariable Long scooterId) {
        Scooter scooter = scooterService.getScooterById(scooterId);
        if (scooter.getRentalPrice() != null) {
            scooter.getRentalPrice().add(linkTo(methodOn(RentalPriceController.class)
                    .getRentalPriceOfScooter(country, region, city, rentalPointId, scooterId,
                            scooter.getRentalPrice().getId()))
                    .withRel("to_price_set")
                    .withType("GET"));
        } else {
            scooter.add(linkTo(ScooterController.class)
                    .slash("/" + country + "/" + region + "/" + city + "/" + rentalPointId + "/scooters/" +
                            scooterId + "/price")
                    .withRel("set_rental_price_fot_this_scooter")
                    .withType("PUT"));
        }
        scooter.add(linkTo(methodOn(RentHistoryController.class)
                .getVariationsToRentScooter(country, region, city, rentalPointId, scooterId))
                .withRel("variations_to_rent_scooter")
                .withType("GET"));
        scooter.add(linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + "/" + region + "/" + city + "/" + rentalPointId + "/scooters/" + scooterId)
                .withRel("update_this_scooter")
                .withType("PUT"));
        scooter.add(linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + "/" + region + "/" + city + "/" + rentalPointId + "/scooters/" +
                        scooterId + "/history")
                .withRel("rent_history_of_this_scooter")
                .withType("GET"));
        scooter.add(linkTo(methodOn(ScooterController.class)
                .deleteScooter(country, region, city, rentalPointId, scooterId))
                .withRel("delete_this_scooter")
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
        Scooter scooterFromDB = scooterService.createNewScooter(scooter);
        return ResponseEntity.status(HttpStatus.CREATED).body(scooterFromDB);
    }

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

package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.service.CatalogService;
import com.scooterrental.scooter_rental.service.RentalPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/rental_points")
public class CatalogController {

    private final CatalogService catalogService;
    private final RentalPointService rentalPointService;

    public CatalogController(CatalogService catalogService, RentalPointService rentalPointService) {
        this.catalogService = catalogService;
        this.rentalPointService = rentalPointService;
    }

    @GetMapping
    public ResponseEntity<List<CatalogDTO>> getLevelWithoutParent() {
        List<CatalogDTO> catalogList = catalogService.findAllParentIdIsNull();
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points")
                    .slash(catalog.getTitle().toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus")
    public ResponseEntity<List<CatalogDTO>> getBelarusChildren() {
        List<CatalogDTO> catalogList = catalogService.findAllSecondLevelTree("Belarus");
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = 0;
            if (catalog.getTitle().contains("Region")) {
                childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
            } else {
                childrenOfItem = rentalPointService.countRentalPointByCityId(catalog.getId());
//                for (RentalPoint rentalPoint : catalog.getRentalPoints()) {
//                    Link rentalPointLink = linkTo(StarterController.class)
//                            .slash("/rental_points/belarus")
//                            .slash(catalog.getTitle().toLowerCase())
//                            .slash(rentalPoint.getId())
//                            .withSelfRel();
//                }
            }
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus")
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Elements in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

    @GetMapping("belarus/{region}")
    public ResponseEntity<List<CatalogDTO>> getBrestRegionChildren(@PathVariable String region) {
        String pathString = catalogService.getPathString(region);
        List<CatalogDTO> catalogList = catalogService.findAllSecondLevelTree(pathString);
        for (CatalogDTO catalog : catalogList) {
            Integer childrenOfItem = rentalPointService.countRentalPointByCityId(catalog.getId());
            Link catalogItem = linkTo(StarterController.class)
                    .slash("/rental_points/belarus/" + region)
                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
                    .withRel("Rental points in section: " + childrenOfItem);
            catalog.add(catalogItem);
        }

        return ResponseEntity.ok(catalogList);
    }

}

package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.RentalPoint;
import com.scooterrental.scooter_rental.service.RentalPointService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/rental_points/belarus")
public class RentalPointController {

    private final RentalPointService rentalPointService;

    public RentalPointController(RentalPointService rentalPointService) {
        this.rentalPointService = rentalPointService;
    }

//    @GetMapping("/minsk_region/salihorsk")
//    public ResponseEntity<List<RentalPoint>> getAllSalihorskRentalPoints() {
//        List<RentalPoint> catalogList = rentalPointService.findAllSecondLevelTree("Salihorsk");
//        for (Catalog catalog : catalogList) {
//            Integer childrenOfItem = catalogService.countChildrenOfItem(catalog.getId());
//            Link catalogItem = linkTo(StarterController.class)
//                    .slash("/rental_points/belarus/minsk_region/salihorsk")
//                    .slash(catalog.getTitle().replace(" ", "_").toLowerCase())
//                    .withRel("Elements in section: " + childrenOfItem);
//            catalog.add(catalogItem);
//        }
//
//        return ResponseEntity.ok(catalogList);
//    }
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

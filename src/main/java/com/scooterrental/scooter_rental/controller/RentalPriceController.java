package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.service.RentalPriceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RentalPriceController {

    private final RentalPriceService rentalPriceService;

    public RentalPriceController(RentalPriceService rentalPriceService) {
        this.rentalPriceService = rentalPriceService;
    }

    @GetMapping("/rental_points/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}/{priceId}")
    public ResponseEntity<RentalPrice> getRentalPriceOfScooter(@PathVariable String country,
                                                               @PathVariable String region,
                                                               @PathVariable String city,
                                                               @PathVariable Long rentalPointId,
                                                               @PathVariable Long scooterId,
                                                               @PathVariable Long priceId) {
        RentalPrice rentalPrice = rentalPriceService.getRentalPriceById(priceId);
        rentalPrice.add(linkTo(methodOn(RentalPriceController.class)
                .deleteRentalPrice(priceId))
                .withRel("delete_rental_price")
                .withType("DELETE"));
        rentalPrice.add(linkTo(RentalPriceController.class)
                .slash("rental_price")
                .withRel("add_rental_price_set")
                .withType("POST"));
        //todo add drop rental price link

        return ResponseEntity.ok(rentalPrice);
    }

    @GetMapping("/rental_price")
    public ResponseEntity<List<RentalPrice>> findAllRentalPriceSets() {
        List<RentalPrice> priceList = rentalPriceService.findAllRentalPrice();
        for (RentalPrice rentalPrice : priceList) {
            rentalPrice.add(linkTo(RentalPriceController.class)
                    .slash("/rental_price/" + rentalPrice.getId())
                    .withSelfRel());
        }

        return ResponseEntity.ok(priceList);
    }

    @GetMapping("rental_price/{priceId}")
    public ResponseEntity<RentalPrice> getRentalPrice(@PathVariable Long priceId) {
        RentalPrice rentalPrice = rentalPriceService.getRentalPriceById(priceId);
        rentalPrice.add(linkTo(methodOn(RentalPriceController.class)
                .deleteRentalPrice(priceId))
                .withRel("delete_rental_price")
                .withType("DELETE"));
        rentalPrice.add(linkTo(StarterController.class).withRel("to_start_page"));

        return ResponseEntity.ok(rentalPrice);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/rental_price/{priceId}")
    public ResponseEntity<Long> deleteRentalPrice(@PathVariable Long priceId) {
        rentalPriceService.deleteRentalPriceById(priceId);

        return new ResponseEntity<>(priceId, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/rental_price")
    public ResponseEntity<RentalPrice> addRentalPriceSet(@RequestBody RentalPrice rentalPrice) {
        RentalPrice price = rentalPriceService.saveRentalPrice(rentalPrice);

        return ResponseEntity.ok(price);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/rental_price/{priceId}")
    public ResponseEntity<RentalPrice> updateRentalPriceSet(@RequestBody RentalPrice rentalPrice,
                                                            @PathVariable Long priceId) {
        rentalPrice.setId(priceId);
        RentalPrice price = rentalPriceService.saveRentalPrice(rentalPrice);

        return ResponseEntity.ok(price);
    }

//    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
//    @PutMapping("/{country}/{region}/{city}/{rentalPointId}/scooters/{scooterId}")
//    public ResponseEntity<RentalPrice> updateRentalPriceForScooter(@PathVariable String country,
//                                                                        @PathVariable String region,
//                                                                        @PathVariable String city,
//                                                                        @PathVariable Long rentalPointId,
//                                                                        @PathVariable Long scooterId) {
//
//
//    }
}

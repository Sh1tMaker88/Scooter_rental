package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.security.controller.UserController;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class StarterController {

    @GetMapping
    public ResponseEntity<CatalogDTO> starterPage() {
        Link link = linkTo(UserController.class).withRel("users");
        Link linkTwo = linkTo(CatalogController.class).withRel("rental_points");
        Link linkThree = linkTo(RentalPriceController.class).slash("rental_price").withRel("rental_price_sets");
        CatalogDTO catalog = new CatalogDTO();
        catalog.setId(0L);
        catalog.setTitle("Starter page");
        catalog.add(link, linkTwo, linkThree);

        return ResponseEntity.ok(catalog);
    }
}

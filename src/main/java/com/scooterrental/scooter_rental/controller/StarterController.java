package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.security.controller.UserController;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class StarterController {

    @GetMapping
    public ResponseEntity<Catalog> starterPage() {
        Link link = linkTo(UserController.class).withRel("users");
        Link linkTwo = linkTo(CatalogController.class).withRel("rental_points");
        Catalog catalog = new Catalog();
        catalog.add(link);
        catalog.add(linkTwo);

        return ResponseEntity.ok(catalog);
    }
}

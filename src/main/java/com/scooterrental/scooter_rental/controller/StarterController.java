package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.Catalog;
import com.scooterrental.scooter_rental.model.dto.CatalogDTO;
import com.scooterrental.scooter_rental.model.dto.StarterDTO;
import com.scooterrental.scooter_rental.security.controller.UserController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class StarterController {

    @GetMapping
    public ResponseEntity<EntityModel<StarterDTO>> starterPage() {
        Link link = linkTo(UserController.class).withRel("users");
        Link linkTwo = linkTo(CatalogController.class).withRel("rental_points");
        Link linkThree = linkTo(RentalPriceController.class).slash("rental_price").withRel("rental_price_sets");
        Link linkFour = linkTo((RentHistoryController.class)).slash("/rent_history").withRel("rent_history");
        StarterDTO starterDTO = new StarterDTO();
        starterDTO.setTitle("Starter page");
        List<Link> linkList = Arrays.asList(link, linkTwo, linkFour, linkThree);

        return ResponseEntity.ok(EntityModel.of(starterDTO, linkList));
    }
}

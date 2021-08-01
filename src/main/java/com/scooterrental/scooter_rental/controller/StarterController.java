package com.scooterrental.scooter_rental.controller;

import com.scooterrental.scooter_rental.model.dto.StarterDTO;
import com.scooterrental.scooter_rental.security.controller.UserController;
import com.scooterrental.scooter_rental.security.service.UserService;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class StarterController {

    private final UserService userService;

    public StarterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<EntityModel<StarterDTO>> starterPage() {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Long userId = userService.findByUsername(principal.getName()).getId();
        Link selfLink = linkTo(UserController.class).slash("/" + userId).withSelfRel();
        Link link = linkTo(UserController.class).withRel("users");
        Link linkTwo = linkTo(CatalogController.class).withRel("rental_points");
        Link linkThree = linkTo(RentalPriceController.class).slash("rental_price").withRel("rental_price_sets");
        Link linkFour = linkTo((RentHistoryController.class)).slash("/rent_history").withRel("rent_history");
        StarterDTO starterDTO = new StarterDTO();
        starterDTO.setTitle("Starter page");
        List<Link> linkList = Arrays.asList(selfLink, link, linkTwo, linkFour, linkThree);

        return ResponseEntity.ok(EntityModel.of(starterDTO, linkList));
    }
}

package com.scooterrental.scooter_rental.controller;

import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
public class StarterController {

    @GetMapping
    public ResponseEntity starterPage() {
        Link link = new Link("http://localhost:8080/users");
        return ResponseEntity.ok(link);
    }
}

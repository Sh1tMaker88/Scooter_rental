package com.scooterrental.scooter_rental.util;

import com.scooterrental.scooter_rental.controller.RentHistoryController;
import com.scooterrental.scooter_rental.controller.RentalPointController;
import com.scooterrental.scooter_rental.controller.ScooterController;
import com.scooterrental.scooter_rental.model.dto.RentHistoryDTO;
import com.scooterrental.scooter_rental.security.controller.UserController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class ControllerUtil {

    public static void setLinksForRentHistoryRepresentation(String country, String region, String city,
                                                      Long rentalPointId, RentHistoryDTO rentHistoryDTO) {
        rentHistoryDTO.getRentalPointId().add(WebMvcLinkBuilder.linkTo(methodOn(RentalPointController.class)
                .getRentalPointRepresentation(country, region, city, rentalPointId))
                .withSelfRel()
                .withType("GET"));
        rentHistoryDTO.getScooterId().add(WebMvcLinkBuilder.linkTo(methodOn(ScooterController.class).getScooter(country, region,
                city, rentalPointId, rentHistoryDTO.getScooterId().getId()))
                .withSelfRel()
                .withType("GET"));
        rentHistoryDTO.getUserId().add(linkTo(methodOn(UserController.class)
                .getUserById(rentHistoryDTO.getUserId().getId()))
                .withSelfRel()
                .withType("GET"));
    }

    public static List<Link> getLinksToRent(String country, String region, String city, Long rentalPointId, Long scooterId) {
        Link link1 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/one_hour")
                .withRel("one_hour")
                .withType("POST");
        Link link2 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/two_hours")
                .withRel("two_hour")
                .withType("POST");
        Link link3 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/three_hours")
                .withRel("three_hour")
                .withType("POST");
        Link link4 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/one_day")
                .withRel("one_day")
                .withType("POST");
        Link link5 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/two_days")
                .withRel("one_days")
                .withType("POST");
        Link link6 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/week")
                .withRel("week")
                .withType("POST");
        Link link7 = linkTo(RentHistoryController.class)
                .slash("/rental_points/" + country + "/" + region + "/" + city + "/" + rentalPointId +
                        "/scooters/" + scooterId + "/rent/month")
                .withRel("month")
                .withType("POST");
        return Arrays.asList(link1, link2, link3, link4, link5, link6, link7);
    }

    public static PageRequest getPageRequestWithPaginationAndSort(Integer page, Integer pageSize, List<String> sort) {
        if (sort.get(1).toLowerCase().equals("desc") || sort.get(1).toLowerCase().equals("dsc") ||
                sort.get(1).toLowerCase().equals("descending")) {
            return PageRequest.of(page, pageSize, Sort.by(sort.get(0)).descending());
        } else {
            return PageRequest.of(page, pageSize, Sort.by(sort.get(0)).ascending());
        }
    }
}

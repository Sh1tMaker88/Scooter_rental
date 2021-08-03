package com.scooterrental.scooter_rental.model.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.scooterrental.scooter_rental.model.Catalog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;
import java.util.Objects;

@Setter
@Getter
public class RentalPointDTO extends RepresentationModel<RentalPointDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("location")
    private Point location;

    @JsonProperty("city")
    private CityDTO city;

    @JsonProperty("street")
    private String street;

    @JsonProperty("houseNumber")
    private Integer houseNumber;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("email")
    private String email;

    @JsonProperty("balance")
    private Double balance;
}

package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Getter
@Setter
public class RentalPointWithScooterDTO extends RepresentationModel<RentalPointWithScooterDTO> {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("scooterList")
    private List<ScooterDTO> scooterList;

    @JsonProperty("location")
    private LocationDTO location;

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
}

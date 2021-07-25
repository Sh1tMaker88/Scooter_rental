package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@Setter
@Getter
public class RentHistoryDTO extends RepresentationModel<RentHistoryDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("rentType")
    private String rentType;

    @JsonProperty("price")
    private Double price;

    @JsonProperty("rentDate")
    private LocalDateTime rentDate;

    @JsonProperty("userId")
    private UserSlimDTO userId;

    @JsonProperty("scooterId")
    private ScooterSlimDTO scooterId;

    @JsonProperty("rentalPointId")
    private RentalPointSlimDTO rentalPointId;
}

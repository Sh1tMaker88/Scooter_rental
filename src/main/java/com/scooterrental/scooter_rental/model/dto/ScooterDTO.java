package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scooterrental.scooter_rental.model.ScooterStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScooterDTO extends RepresentationModel<ScooterDTO> {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("serialNumber")
    private String serialNumber;

    @JsonProperty("rentalPrice")
    private PriceTitleDTO rentalPrice;

    @JsonProperty("status")
    private ScooterStatus status;

    @JsonProperty("enginePower")
    private Integer enginePower;

    @JsonProperty("maxSpeed")
    private Integer maxSpeed;

    @JsonProperty("batteryCapacity")
    private Double batteryCapacity;

    @JsonProperty("wheelsDiameter")
    private Double wheelsDiameter;

    @JsonProperty("singleChargeDistance")
    private Integer singleChargeDistance;

    @JsonProperty("weight")
    private Double weight;
}

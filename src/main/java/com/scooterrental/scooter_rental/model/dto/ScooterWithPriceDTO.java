package com.scooterrental.scooter_rental.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.scooterrental.scooter_rental.model.RentalPrice;
import com.scooterrental.scooter_rental.model.ScooterStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScooterWithPriceDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("rentalPrice")
    private RentalPrice rentalPrice;

    @JsonProperty("serialNumber")
    private String serialNumber;

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

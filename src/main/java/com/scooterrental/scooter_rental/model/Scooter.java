package com.scooterrental.scooter_rental.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "scooter")
public class Scooter extends RepresentationModel<Scooter> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ScooterStatus status = ScooterStatus.AVAILABLE;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "rental_point_id")
    private RentalPoint rentalPoint;

//    @JsonIdentityReference(alwaysAsId = true)
    @JsonIgnoreProperties("scooter")
    @ManyToOne
    @JoinColumn(name = "price_id")
    private RentalPrice rentalPrice;

    @Column(name = "engine_power")
    private Integer enginePower;

    @Column(name = "max_speed")
    private Integer maxSpeed;

    @Column(name = "battery_capacity")
    private Double batteryCapacity;

    @Column(name = "wheels_diameter")
    private Double wheelsDiameter;

    @Column(name = "single_charge_distance")
    private Integer singleChargeDistance;

    @Column(name = "weight")
    private Double weight;

    @Override
    public String toString() {
        return "Scooter{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", status=" + status +
                ", rentalPoint=" + rentalPoint +
                ", rentalPrice=" + rentalPrice +
                ", enginePower=" + enginePower +
                ", maxSpeed=" + maxSpeed +
                ", batteryCapacity=" + batteryCapacity +
                ", wheelsDiameter=" + wheelsDiameter +
                ", singleChargeDistance=" + singleChargeDistance +
                ", weight=" + weight +
                '}';
    }
}

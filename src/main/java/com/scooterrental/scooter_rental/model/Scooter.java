package com.scooterrental.scooter_rental.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "scooter")
public class Scooter {

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

    @ManyToOne
    @JoinColumn(name = "rental_point_id")
    private RentalPoint rentalPoint;

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

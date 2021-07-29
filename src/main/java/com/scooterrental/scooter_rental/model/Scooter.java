package com.scooterrental.scooter_rental.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "title")
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

    @JsonIgnore
    @OneToMany(mappedBy = "scooterId")
    private List<RentHistory> rentHistoryList = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Scooter scooter = (Scooter) o;
        return Objects.equals(id, scooter.id) &&
                Objects.equals(brand, scooter.brand) &&
                Objects.equals(model, scooter.model) &&
                Objects.equals(serialNumber, scooter.serialNumber) &&
                status == scooter.status &&
                Objects.equals(rentalPoint, scooter.rentalPoint) &&
                Objects.equals(rentalPrice, scooter.rentalPrice) &&
                Objects.equals(enginePower, scooter.enginePower) &&
                Objects.equals(maxSpeed, scooter.maxSpeed) &&
                Objects.equals(batteryCapacity, scooter.batteryCapacity) &&
                Objects.equals(wheelsDiameter, scooter.wheelsDiameter) &&
                Objects.equals(singleChargeDistance, scooter.singleChargeDistance) &&
                Objects.equals(weight, scooter.weight) &&
                Objects.equals(rentHistoryList, scooter.rentHistoryList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, brand, model, serialNumber, status, rentalPoint, rentalPrice, enginePower, maxSpeed, batteryCapacity, wheelsDiameter, singleChargeDistance, weight, rentHistoryList);
    }

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
                ", rentHistoryList=" + rentHistoryList +
                "} " + super.toString();
    }
}

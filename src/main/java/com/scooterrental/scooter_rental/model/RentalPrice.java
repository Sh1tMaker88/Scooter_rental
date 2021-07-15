package com.scooterrental.scooter_rental.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "rental_price")
public class RentalPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @OneToMany(mappedBy = "rentalPrice")
    private List<Scooter> scooter;

    @Column(name = "one_hour")
    private Double oneHour;

    @Column(name = "two_hours")
    private Double twoHours;

    @Column(name = "three_hours")
    private Double threeHours;

    @Column(name = "one_day")
    private Double oneDay;

    @Column(name = "two_days")
    private Double twoDays;

    @Column(name = "week")
    private Double week;

    @Column(name = "month")
    private Double month;

    @Override
    public String toString() {
        return "RentalPrice{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", scooter=" + scooter +
                ", oneHour=" + oneHour +
                ", twoHours=" + twoHours +
                ", threeHours=" + threeHours +
                ", oneDay=" + oneDay +
                ", twoDays=" + twoDays +
                ", week=" + week +
                ", month=" + month +
                '}';
    }
}

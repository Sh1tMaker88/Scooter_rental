package com.scooterrental.scooter_rental.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.geo.Point;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rental_point")
public class RentalPoint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "owner")
    private String owner;

    @OneToMany(mappedBy = "rentalPoint")
    private List<Scooter> scooterList;

    @Column(name = "location")
    private Point location;

    @ManyToOne
    @JoinColumn(name = "city_id")
    private Catalog city;

    @Column(name = "street")
    private String street;

    @Column(name = "house_number")
    private Integer houseNumber;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email")
    private String email;

    @Override
    public String toString() {
        return "RentalPoint{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner='" + owner + '\'' +
                ", scooterList=" + scooterList +
                ", location=" + location +
                ", city=" + city +
                ", street='" + street + '\'' +
                ", houseNumber=" + houseNumber +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

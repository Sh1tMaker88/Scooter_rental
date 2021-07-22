package com.scooterrental.scooter_rental.model;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rental_point")
public class RentalPoint extends RepresentationModel<RentalPoint> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "owner")
    private String owner;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "model")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "rentalPoint")
    private List<Scooter> scooterList;

//    @JsonIgnore
//    @Transient
//    @JsonDeserialize(using = GeometryDeserializer.class)
    @Column(name = "location")
    private Point location;

    @JsonIgnoreProperties("rentalPoints")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "title")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(fetch = FetchType.LAZY)
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
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalPoint that = (RentalPoint) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(owner, that.owner) &&
//                Objects.equals(location, that.location) &&
                Objects.equals(city, that.city) &&
                Objects.equals(street, that.street) &&
                Objects.equals(houseNumber, that.houseNumber) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, owner, city, street, houseNumber, phoneNumber, email);
    }
}

package com.scooterrental.scooter_rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "catalog")
public class Catalog extends RepresentationModel<Catalog> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH},
            mappedBy = "city")
    private List<RentalPoint> rentalPoints = new ArrayList<>();

    public void addRentalPoint(RentalPoint rentalPoint) {
        rentalPoints.add(rentalPoint);
        rentalPoint.setCity(this);
    }

    public void removeRentalPoint(RentalPoint rentalPoint) {
        rentalPoints.remove(rentalPoint);
        rentalPoint.setCity(null);
    }
}

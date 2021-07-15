package com.scooterrental.scooter_rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "catalog")
public class Catalog extends RepresentationModel<Catalog> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "parent_id")
    private Long parentId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "city")
    private List<RentalPoint> rentalPoints;

    public void addRentalPointToCatalogItem(RentalPoint rentalPoint) {
        if (rentalPoints == null) {
            rentalPoints = new ArrayList<>();
        }
        rentalPoints.add(rentalPoint);
        rentalPoint.setCity(this);
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", parentId=" + parentId +
                ", rentalPoints=" + rentalPoints +
                "} " + super.toString();
    }
}

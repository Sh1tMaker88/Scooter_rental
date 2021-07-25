package com.scooterrental.scooter_rental.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Catalog catalog = (Catalog) o;
        return Objects.equals(id, catalog.id) &&
                Objects.equals(title, catalog.title) &&
                Objects.equals(parentId, catalog.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, parentId);
    }
}

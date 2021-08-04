package com.scooterrental.scooter_rental.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
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

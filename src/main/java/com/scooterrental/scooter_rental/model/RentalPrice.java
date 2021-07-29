package com.scooterrental.scooter_rental.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "rental_price")
public class RentalPrice extends RepresentationModel<RentalPrice> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @JsonIgnore
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RentalPrice that = (RentalPrice) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(title, that.title) &&
                Objects.equals(oneHour, that.oneHour) &&
                Objects.equals(twoHours, that.twoHours) &&
                Objects.equals(threeHours, that.threeHours) &&
                Objects.equals(oneDay, that.oneDay) &&
                Objects.equals(twoDays, that.twoDays) &&
                Objects.equals(week, that.week) &&
                Objects.equals(month, that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, title, oneHour, twoHours, threeHours, oneDay, twoDays, week, month);
    }

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
                "} " + super.toString();
    }
}

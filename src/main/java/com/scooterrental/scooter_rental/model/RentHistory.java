package com.scooterrental.scooter_rental.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.scooterrental.scooter_rental.security.model.User;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "rent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RentHistory extends RepresentationModel<RentHistory> {

    public RentHistory(String rentType, Double price, User userId, Scooter scooterId,
                       RentalPoint rentalPointId) {
        this.rentType = rentType;
        this.price = price;
        this.userId = userId;
        this.scooterId = scooterId;
        this.rentalPointId = rentalPointId;
        this.rentDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "rent_type")
    private String rentType;

    @Column(name = "price")
    private Double price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Column(name = "rent_date")
    private LocalDateTime rentDate;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIgnoreProperties("rentHistoryList")
    @ManyToOne
    @JoinColumn(name = "scooter_id")
    private Scooter scooterId;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIgnoreProperties("rentHistoryList")
    @ManyToOne
    @JoinColumn(name = "rental_point_id")
    private RentalPoint rentalPointId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentHistory that = (RentHistory) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(rentType, that.rentType) &&
                Objects.equals(price, that.price) &&
                Objects.equals(rentDate, that.rentDate) &&
                Objects.equals(userId, that.userId) &&
                Objects.equals(scooterId, that.scooterId) &&
                Objects.equals(rentalPointId, that.rentalPointId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, rentType, price, rentDate, userId, scooterId, rentalPointId);
    }
}


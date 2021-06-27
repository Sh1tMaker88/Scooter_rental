package com.scooterrental.scooter_rental.security.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {

    @NotEmpty(message = "Username should not be empty")
    @Size(min = 3, max = 30, message = "Username should be between 3 and 30 characters")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "First name should not be empty")
    @Size(min = 3, max = 30, message = "First name should be between 3 and 30 characters")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name should not be empty")
    @Size(min = 3, max = 30, message = "Last name should be between 3 and 30 characters")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Not valid email")
    @Column(name = "email")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private List<Role> roles;
}

package com.scooterrental.scooter_rental.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.scooterrental.scooter_rental.security.model.User;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Date created;
    private Date updated;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUpdated(updated);
        user.setPassword(password);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setCreated(user.getCreated());
        userDto.setUpdated(user.getUpdated());

        return userDto;
    }
}


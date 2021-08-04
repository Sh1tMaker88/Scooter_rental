package com.scooterrental.scooter_rental.security.controller;

import com.scooterrental.scooter_rental.exception.BadRequestException;
import com.scooterrental.scooter_rental.security.dto.UserDto;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
@Slf4j
public class RegisterUserController {

    private final UserService userService;

    public RegisterUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String userRegistration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public ResponseEntity<User> createUser(@ModelAttribute("userForm") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder exceptions = new StringBuilder();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                log.warn(error.getDefaultMessage());
                exceptions.append(error.getDefaultMessage()).append("; ");
            }
            throw new BadRequestException(exceptions.toString());
        }
        User register = userService.register(user);
        return new ResponseEntity<>(register, HttpStatus.OK);
    }
}

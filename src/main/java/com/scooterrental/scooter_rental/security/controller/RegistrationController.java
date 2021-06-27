package com.scooterrental.scooter_rental.security.controller;

import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Controller
@Slf4j
public class RegistrationController {

    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String userRegistration(Model model) {
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(@ModelAttribute("userForm") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            for (ObjectError error : allErrors) {
                log.warn(error.getDefaultMessage());
            }
            return "registration";
        }
        userService.register(user);
        return "redirect:/login";
    }

//    @PostMapping("/registration")
//    public String addUser(User user, Map<String, Object> model) {
//        User userFromDB = userRepository.findByUsername(user.getUsername());
//
//        if (userFromDB != null) {
//            model.put("message", "User exists");
//            return "registration";
//        }
//
////        user.setActive(true);
////        user.setRoles(Collections.singleton(Role.class));
//        userRepository.save(user);
//
//        return "redirect:/login";
//    }
}

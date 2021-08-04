package com.scooterrental.scooter_rental.security.controller;

import com.scooterrental.scooter_rental.exception.BadRequestException;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

//        AdminUserDto result = AdminUserDto.fromUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<User>>> allUsers(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            PagedResourcesAssembler<User> assembler
    ) {
        Page<User> usersPages = userService.getAllUsersWithPagination(pageNumber, pageSize, sortBy);
        if (usersPages.getTotalPages() < pageNumber) {
            throw new BadRequestException("Beyond the number of pages. Total pages=" + usersPages.getTotalPages()
                    + ", requested page=" + pageNumber);
        }
        if (!usersPages.hasContent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for (User user : usersPages.getContent()) {
            Link userSelfLink = linkTo(UserController.class)
                    .slash("/admin/" + user.getId().toString())
                    .withSelfRel();
            user.add(userSelfLink);
            user.setPassword("Not allowed");
        }
        PagedModel<EntityModel<User>> userListModel = assembler.toModel(usersPages);

        return ResponseEntity.ok(userListModel);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUserByAdmin(@RequestBody User user,
                                                  @PathVariable("id") Long id) {
        User check = userService.findById(id);
        if (check == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        user.setId(id);
        User updatedUser = userService.saveUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

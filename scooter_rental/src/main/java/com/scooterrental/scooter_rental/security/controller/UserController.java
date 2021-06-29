package com.scooterrental.scooter_rental.security.controller;

import com.scooterrental.scooter_rental.exception.BadRequestException;
import com.scooterrental.scooter_rental.security.dto.UserDto;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<User>>> allUsers(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "3") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            PagedResourcesAssembler<User> assembler
    ) {
        Page<User> usersPages = userService.getAllUsersWithPagination(pageNumber, pageSize, sortBy);
        if (usersPages.getTotalPages() < pageNumber) {
            log.warn("Beyond the number of pages. Total pages=" + usersPages.getTotalPages() + ", requested page=" + pageNumber);
            throw new BadRequestException("Beyond the number of pages. Total pages=" + usersPages.getTotalPages()
                    + ", requested page=" + pageNumber);
        }
        if (!usersPages.hasContent()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for (User user : usersPages.getContent()) {
            Link userSelfLink = linkTo(UserController.class)
                    .slash(user.getId().toString())
                    .withSelfRel();
            user.add(userSelfLink);
        }
        PagedModel<EntityModel<User>> userListModel = assembler.toModel(usersPages);

        return ResponseEntity.ok(userListModel);
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.body.username == principal.username " +
            "OR hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/{id}")
    @ResponseStatus
    @PreAuthorize("#user.username == principal.username " +
            "OR hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@RequestBody UserDto user,
                                           @PathVariable("id") Long id) {
        User check = userService.findById(id);
        if (check == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

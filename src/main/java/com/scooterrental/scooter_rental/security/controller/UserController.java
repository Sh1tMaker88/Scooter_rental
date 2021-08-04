package com.scooterrental.scooter_rental.security.controller;

import com.scooterrental.scooter_rental.util.ControllerUtil;
import com.scooterrental.scooter_rental.controller.RentHistoryController;
import com.scooterrental.scooter_rental.exception.BadRequestException;
import com.scooterrental.scooter_rental.model.RentHistory;
import com.scooterrental.scooter_rental.security.dto.UserDto;
import com.scooterrental.scooter_rental.security.model.User;
import com.scooterrental.scooter_rental.security.service.UserService;
import com.scooterrental.scooter_rental.service.RentHistoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RentHistoryService rentHistoryService;
    private User user;

    public UserController(UserService userService, RentHistoryService rentHistoryService) {
        this.userService = userService;
        this.rentHistoryService = rentHistoryService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<User>>> allUsers(
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
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
            user.setPassword("Not allowed");
        }
        PagedModel<EntityModel<User>> userListModel = assembler.toModel(usersPages);

        return ResponseEntity.ok(userListModel);
    }

    @GetMapping("/{id}")
    @PostAuthorize("returnObject.body.content.username == principal.username " +
            "OR hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<EntityModel<UserDto>> getUserById(@PathVariable("id") Long id) {
        User user = userService.findById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        UserDto result = UserDto.fromUser(user);
        Link historyOfUser = linkTo(UserController.class)
                .slash("/" + user.getId() + "/history")
                .withRel("rent_history_of_this_user")
                .withType("GET");
        Link editUser = linkTo(UserController.class)
                .slash("/" + user.getId())
                .withRel("update_user")
                .withType("POST");

        return ResponseEntity.ok(EntityModel.of(result, historyOfUser, editUser));
    }

    @PostMapping("/{id}")
    @PreAuthorize("#user.username == principal.username " +
            "OR hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<User> updateUserWithDto(@RequestBody UserDto user,
                                                  @PathVariable("id") Long id) {
        User check = userService.findById(id);
        if (check == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        User updatedUser = userService.updateUserWithDto(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @GetMapping("/{userId}/history")
    @PreAuthorize("@userServiceImpl.findById(#userId).username.equals(principal.username) " +
            "OR hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<RentHistory>>> getUserRentHistory(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id, asc") List<String> sort,
            PagedResourcesAssembler<RentHistory> assembler) {
        User user = userService.findById(userId);
        PageRequest pageRequest = ControllerUtil.getPageRequestWithPaginationAndSort(page, pageSize, sort);
        Page<RentHistory> rentHistoryPage =
                rentHistoryService.getAllRentHistoriesByUsername(pageRequest, user.getUsername());
        for (RentHistory rentHistory : rentHistoryPage.getContent()) {
            rentHistory.add(linkTo(methodOn(RentHistoryController.class)
                    .getRentHistoryById(rentHistory.getId()))
                    .withSelfRel());
        }
        return ResponseEntity.ok(assembler.toModel(rentHistoryPage));
    }
}

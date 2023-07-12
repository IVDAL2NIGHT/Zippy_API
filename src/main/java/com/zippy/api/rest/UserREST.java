package com.zippy.api.rest;

//import com.zippy.api.document.Credential;

import com.zippy.api.document.Credential;
import com.zippy.api.document.User;
import com.zippy.api.dto.updateUserDTO;
import com.zippy.api.models.Address;
import com.zippy.api.models.City;
import com.zippy.api.models.Country;
import com.zippy.api.models.State;
import com.zippy.api.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user")
public class UserREST {

    private final UserService userService;

    @Autowired
    public UserREST(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#credential.userId == #id")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Valid @NotNull @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id, @Valid @RequestBody updateUserDTO dto) {
        User user = userService.getUserById(id);
        Address address = new Address(
                dto.getAddress().getDetail(),
                new City(dto.getAddress().getCity().getId(), dto.getAddress().getCity().getName()),
                new State(dto.getAddress().getState().getId(), dto.getAddress().getState().getName()),
                new Country(dto.getAddress().getCountry().getId(), dto.getAddress().getCountry().getName())
        );

        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setAddress(address);
        user.setOccupation(dto.getOccupation());
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> getUser(@NotNull @Valid @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("#credential.userId == #id")
    public ResponseEntity<?> deleteUser(@NotNull @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

}

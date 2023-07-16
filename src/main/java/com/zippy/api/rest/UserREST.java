package com.zippy.api.rest;

import com.zippy.api.document.Credential;
import com.zippy.api.dto.updateUserDTO;
import com.zippy.api.service.UserService;
import org.bson.types.ObjectId;
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

    public UserREST(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#credential.userId == #id")
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@Valid @NotNull @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id, @Valid @RequestBody updateUserDTO dto) {
        return ResponseEntity.ok(userService.save(
                userService.getById(id)
                        .setEmail(dto.email())
                        .setPhone(dto.phone())
                        .setAddress(dto.address())
                        .setOccupation(dto.occupation())
        ));
    }

    @GetMapping("/{id}")
    @PreAuthorize("#credential.userId == #id or hasAuthority('ADMIN')")
    public ResponseEntity<?> getUser(@NotNull @Valid @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("#credential.userId == #id or hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteUser(@NotNull @AuthenticationPrincipal Credential credential, @PathVariable ObjectId id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

}

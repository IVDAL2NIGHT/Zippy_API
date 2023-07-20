package com.zippy.api.rest;

import com.zippy.api.document.Credential;
import com.zippy.api.dto.UpdateDTO;
import com.zippy.api.service.AuthService;
import com.zippy.api.service.CredentialService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/credentials")
public class CredentialREST {
    private final CredentialService credentialService;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public CredentialREST(PasswordEncoder passwordEncoder, AuthService authService, CredentialService credentialService) {
        this.credentialService = credentialService;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @GetMapping("/check/{username}")
    public ResponseEntity<?> checkUsername(@PathVariable String username) {
        return ResponseEntity.ok(credentialService.existsByUsername(username));
    }

    // This endpoint returns the currently authenticated credential
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Credential credential) {
        return ResponseEntity.ok(credential);
    }

    // This endpoint return the credential by id, while the id is the same of the credential.
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> me(@PathVariable ObjectId id) {
        try{
            return ResponseEntity.ok(credentialService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> updateCredential(@NotNull @AuthenticationPrincipal Credential credential, @NotNull @Valid @RequestBody UpdateDTO dto) {
        return ResponseEntity.ok(credentialService.save(credential
                .setEmail(dto.newEmail())
                .setUsername(dto.newUsername())
        ));
    }


    @PutMapping("/update/password/")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<?> changePassword(@NotNull @AuthenticationPrincipal Credential credential, @NotNull @Valid @RequestBody String newPassword) {
        return ResponseEntity.ok(credentialService.save(
                credential.setPassword(passwordEncoder.encode(newPassword))
        ));
    }

    @PreAuthorize("#credential.userId == #id or hasAuthority('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @NotNull @AuthenticationPrincipal Credential credential,
            @NotNull @RequestBody String password,
            @PathVariable ObjectId id
    ) {
        if (!passwordEncoder.matches(password, credential.getPassword())) {
            return ResponseEntity.badRequest().body("Password is incorrect");
        }
        authService.deleteRefreshTokenByOwner_Id(credential.getId());
        credentialService.delete(credential);
        return ResponseEntity.ok().build();
    }
}
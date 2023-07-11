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

    // This endpoint returns the currently authenticated credential
    @GetMapping("/me")
    public ResponseEntity<?> me(@AuthenticationPrincipal Credential credential) {
        return ResponseEntity.ok(credential);
    }

    // This endpoint return the credential by id, while the id is the same of the credential.
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> me(@PathVariable ObjectId id) {
        return ResponseEntity.ok(credentialService.findById(id));
    }

    @PutMapping("/update/")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateCredential(@NotNull @AuthenticationPrincipal Credential credential, @NotNull @Valid @RequestBody UpdateDTO dto) {
        credential.setEmail(dto.getNewEmail());
        credential.setUsername(dto.getNewUsername());
        credentialService.updateCredential(credential);
        return ResponseEntity.ok(credential);
    }


    @PutMapping("/update/password/")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> changePassword(@NotNull @AuthenticationPrincipal Credential credential, @NotNull @Valid @RequestBody String newPassword) {
        credential.setPassword(passwordEncoder.encode(newPassword));
        credentialService.updateCredential(credential);
        return ResponseEntity.ok(credential);
    }

    @DeleteMapping("/delete/")
    public ResponseEntity<?> delete(@NotNull @AuthenticationPrincipal Credential credential, @NotNull @RequestBody String password) {
        if (!passwordEncoder.matches(password, credential.getPassword())) {
            return ResponseEntity.badRequest().build();
        }
        credentialService.deleteCredential(credential);
        authService.deleteRefreshTokenByOwner_Id(credential.getId());
        return ResponseEntity.ok().build();
    }
}
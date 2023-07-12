package com.zippy.api.rest;

import com.zippy.api.constants.Roles;
import com.zippy.api.document.Credential;
import com.zippy.api.document.RefreshToken;
import com.zippy.api.dto.LoginDTO;
import com.zippy.api.dto.SignupDTO;
import com.zippy.api.dto.TokenDTO;
import com.zippy.api.jwt.JwtHelper;
import com.zippy.api.repository.CredentialRepository;
import com.zippy.api.repository.RefreshTokenRepository;
import com.zippy.api.service.AuthService;
import com.zippy.api.service.CredentialService;
import com.zippy.api.service.UserService;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class AuthREST {
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CredentialRepository credentialRepository;
    private final JwtHelper jwtHelper;
    private final CredentialService credentialService;
    private final UserService userService;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    public AuthREST(
            AuthenticationManager authenticationManager,
            RefreshTokenRepository refreshTokenRepository,
            CredentialRepository credentialRepository,
            JwtHelper jwtHelper,
            CredentialService credentialService,
            UserService userService,
            AuthService authService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenRepository = refreshTokenRepository;
        this.credentialRepository = credentialRepository;
        this.jwtHelper = jwtHelper;
        this.credentialService = credentialService;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authService = authService;
    }

    @NotNull
    private ResponseEntity<?> getResponseEntity(Credential credential) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setOwner(credential);
        refreshTokenRepository.save(refreshToken);

        String accessToken = jwtHelper.generateAccessToken(credential);
        String refreshTokenString = jwtHelper.generateRefreshToken(credential, refreshToken);

        return ResponseEntity.ok(new TokenDTO(credential.getId(), accessToken, refreshTokenString));
    }

    @PostMapping("/login")
    @Transactional
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Credential credential = (Credential) authentication.getPrincipal();

        return getResponseEntity(credential);
    }

    @PostMapping("signup")
    @Transactional
    public ResponseEntity<?> signup(@NotNull @Valid @RequestBody SignupDTO dto) {
        if (credentialRepository.existsByEmail(dto.getCredential().getEmail())) {
            return ResponseEntity.badRequest().body("El correo electrónico ya existe");
        }
        if (credentialRepository.existsByUsername(dto.getCredential().getUsername())) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya existe");
        }
        return getResponseEntity(
                credentialRepository.save(
                        new Credential(
                                new ObjectId(),
                                dto.getCredential().getUsername(),
                                dto.getCredential().getEmail(),
                                passwordEncoder.encode(dto.getCredential().getPassword()),
                                Roles.CLIENT,
                                userService.createNewUser(dto.getUser()).getId()
                        )
                )
        );
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (!jwtHelper.validateRefreshToken(refreshTokenString) || !refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            throw new BadCredentialsException("invalid token");
        } else {
            // valid and exists in db
            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

    }

    @PostMapping("logout-all")
    public ResponseEntity<?> logoutAll(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (!jwtHelper.validateRefreshToken(refreshTokenString) || !refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            throw new BadCredentialsException("invalid token");
        } else {
            // valid and exists in db
            authService.deleteRefreshTokenByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            refreshTokenRepository.deleteByOwner_Id(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            return ResponseEntity.ok().build();
        }

    }

    @PostMapping("access-token")
    public ResponseEntity<?> accessToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            Credential credential = credentialService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));
            String accessToken = jwtHelper.generateAccessToken(credential);

            return ResponseEntity.ok(new TokenDTO(credential.getId(), accessToken, refreshTokenString));
        }

        throw new BadCredentialsException("invalid token");
    }

    @PostMapping("refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenDTO dto) {
        String refreshTokenString = dto.getRefreshToken();
        if (jwtHelper.validateRefreshToken(refreshTokenString) && refreshTokenRepository.existsById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString))) {
            // valid and exists in db

            refreshTokenRepository.deleteById(jwtHelper.getTokenIdFromRefreshToken(refreshTokenString));

            Credential credential = credentialService.findById(jwtHelper.getUserIdFromRefreshToken(refreshTokenString));

            return getResponseEntity(credential);
        }

        throw new BadCredentialsException("invalid token");
    }
}
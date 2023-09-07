package com.zippy.api.service;

import com.zippy.api.document.Credential;
import com.zippy.api.repository.CredentialRepository;
import org.bson.types.ObjectId;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CredentialService implements UserDetailsService {
    private final CredentialRepository credentialRepository;
    private final UserService userService;

    public CredentialService(CredentialRepository credentialRepository, UserService userService) {
        this.credentialRepository = credentialRepository;
        this.userService = userService;
    }

    @Override
    public Credential loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found"));
    }

    public Boolean existsByUsername(String username) {
        return credentialRepository.existsByUsername(username);
    }
    public Credential add(Credential credential) {
        return credentialRepository.insert(credential);
    }

    public Credential save(Credential credential) {
        return credentialRepository.save(credential);
    }

    public Credential getById(ObjectId id) throws  UsernameNotFoundException {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
    }

    public void delete(Credential credential) {
        userService.delete(credential.getUserId());
        credentialRepository.deleteById(credential.getId());
    }
}
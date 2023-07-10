package com.zippy.api.service;

import com.zippy.api.document.Credential;
import com.zippy.api.repository.CredentialRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CredentialService implements UserDetailsService {
    @Autowired
    CredentialRepository credentialRepository;

    @Override
    public Credential loadUserByUsername(String username) throws UsernameNotFoundException {
        return credentialRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found")).setAuthorities();
    }

    public Credential findById(ObjectId id) {
        return credentialRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user id not found"));
    }
}
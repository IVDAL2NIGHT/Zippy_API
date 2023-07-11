package com.zippy.api.service;

import com.zippy.api.repository.RefreshTokenRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public void deleteRefreshTokenByOwner_Id(ObjectId userId) {
        refreshTokenRepository.deleteByOwner_Id(userId);
    }
}

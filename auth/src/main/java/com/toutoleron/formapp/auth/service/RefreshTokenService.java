package com.toutoleron.formapp.auth.service;

import com.toutoleron.formapp.auth.dao.RefreshTokenDao;
import com.toutoleron.formapp.auth.dao.UserDao;
import com.toutoleron.formapp.auth.exception.TokenRefreshException;
import com.toutoleron.formapp.auth.model.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${security.refreshTokenDuration}")
    private Long refreshTokenDurationMs;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private UserDao userDao;
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenDao.findByToken(token);
    }

    public RefreshToken createRefreshToken(String username) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userDao.findByEmail(username).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenDao.save(refreshToken);
        return refreshToken;
    }
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenDao.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        return refreshTokenDao.deleteByUser(userDao.findById(userId).get());
    }
}

package com.toutoleron.formapp.auth.dao;

import com.toutoleron.formapp.auth.model.RefreshToken;
import com.toutoleron.formapp.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenDao extends JpaRepository<RefreshToken, Integer> {

    int deleteByUser(User user);

    Optional<RefreshToken> findByToken(String token);
}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AuthUser2FA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class AuthCustomer2FARepository {

    private final Logger logger = LoggerFactory.getLogger(AuthCustomer2FARepository.class);

    @Cacheable(cacheNames = "tasks", key = "#login")
    public AuthUser2FA findByLogin(String login, AuthUser2FA user) {
        logger.debug("> load user by login to cache (when the user was confirm authentication)");
        return user;
    }

    @Cacheable(cacheNames = "tasks", key = "#token")
    public AuthUser2FA findByToken(String token, AuthUser2FA user) {
        logger.debug("> load user by token to cache (when the user was successfully authentication)");
        return user;
    }

    @Cacheable(cacheNames = "tasks", key = "#code")
    public AuthUser2FA findByCode(long code, AuthUser2FA user) {
        logger.debug("> load user by code to cache (when the user was confirm authentication)");
        return user;
    }
}

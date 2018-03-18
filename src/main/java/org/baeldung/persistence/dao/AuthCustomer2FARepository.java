package org.baeldung.persistence.dao;

import org.baeldung.persistence.model.AuthUser2FA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class AuthCustomer2FA {

    private final Logger logger = LoggerFactory.getLogger(AuthCustomer2FA.class);

    @Cacheable(cacheNames = "tasks", key = "#token")
    public AuthUser2FA findByToken(String token, AuthUser2FA dto) {
        logger.debug("> load user to cache (when the user was successfully authentication)");
        return dto;
    }

}

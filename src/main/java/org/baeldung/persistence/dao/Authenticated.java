package org.baeldung.persistence.dao;

import org.baeldung.web.dto.AuthUserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class Authenticated {

    private final Logger logger = LoggerFactory.getLogger(Authenticated.class);

    @Cacheable(cacheNames = "tasks", key = "#token")
    public AuthUserDTO findByToken(String token, AuthUserDTO dto) {
        logger.info("> load user to cache (then the user was successfully authentication)");
        return dto;
    }

}

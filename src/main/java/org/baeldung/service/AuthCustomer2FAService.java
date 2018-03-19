package org.baeldung.service;

import org.apache.commons.codec.DecoderException;
import org.baeldung.persistence.dao.AuthCustomer2FARepository;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.security.totp.util.TOTPCustomUtils;
import org.baeldung.web.dto.AuthCode2FA;
import org.baeldung.persistence.model.AuthUser2FA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthCustomer2FAService {

    private final Logger logger = LoggerFactory.getLogger(AuthCustomer2FAService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthCustomer2FARepository authCustomer2faRepository;

    public String getTokenAuthentication(AuthUser2FA user2fa) throws DecoderException, GeneralSecurityException {
        Optional<User> user = findByUser(user2fa);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            String secret = TOTPCustomUtils.generateSecret();
            user2fa.setSecret(secret);
            authCustomer2faRepository.findByToken(token, user2fa);
            logger.debug(">>> You to send a verification CODE: {}", TOTPCustomUtils.getRfcOTPCode(secret));
            return token;
        }
        return null;
    }

    public boolean getUserConfirmAuthentication(String token, AuthCode2FA code) throws DecoderException, GeneralSecurityException {
        AuthUser2FA user = findByToken(token);
        if (user != null
                && TOTPCustomUtils.checkCode(user.getSecret(), code.getCode())) {
            authCustomer2faRepository.findByCode(code.getCode(), user);
            return true;
        }
        return false;
    }

    public Optional<User> findByUser(AuthUser2FA user2fa) {
        logger.debug("> Step #1: to do user authentication by email & password");

        Optional<User> user = Optional.of(repository.findByEmail(user2fa.getLogin()));
        if (user.isPresent()
                && passwordEncoder.matches(user2fa.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public AuthUser2FA findByToken(String token) {
        logger.debug("> Step #2: get successfully authentication to user by token from cache");

        return authCustomer2faRepository.findByToken(token, null);
    }

    public AuthUser2FA findByCode(long code) {
        logger.debug("> Step #3: get authorization to user by code from cache");

        return authCustomer2faRepository.findByCode(code, null);
    }
}

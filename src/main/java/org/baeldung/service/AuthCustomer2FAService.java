package org.baeldung.service;

import org.apache.commons.codec.DecoderException;
import org.baeldung.persistence.dao.AuthCustomer2FA;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.security.totp.util.TOTPCustomUtils;
import org.baeldung.web.dto.AuthCode;
import org.baeldung.persistence.model.AuthUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.GeneralSecurityException;
import java.util.UUID;

@Service
public class AuthCustomer2FAService {

    private final Logger logger = LoggerFactory.getLogger(AuthCustomer2FAService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repository;

    @Autowired
    private AuthCustomer2FA authenticated;

    public String authentication(AuthUser authUser) throws DecoderException, GeneralSecurityException {
        logger.debug("> Step #1: to do user authentication by login & password");

        User user = findByAuthUser(authUser);
        if (user!=null) {
            String token = UUID.randomUUID().toString();
            String secret = TOTPCustomUtils.generateSecret();
            authUser.setSecret(secret);
            authenticated.findByToken(token, authUser);
            logger.debug("> You to send a verification CODE: {}", TOTPCustomUtils.getRfcOTPCode(secret));
            return token;
        }
        return null;
    }

    public AuthUser authenticated(String token) {
        logger.debug("> Step #2: get successfully authentication of user from cache");
        return authenticated.findByToken(token, null);
    }

    public AuthUser authenticated2fa(String token, AuthCode authCode) throws DecoderException, GeneralSecurityException {
        AuthUser authUser = authenticated(token);
        if (authUser !=null) {
            if (TOTPCustomUtils.checkCode(authUser.getSecret(), authCode.getCode())) {
                return authUser;
            }
        }
        return null;
    }

    private User findByAuthUser(AuthUser authUser) {
        User user = repository.findByEmail(authUser.getLogin());

        if (user!=null
                && passwordEncoder.matches(authUser.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }
}

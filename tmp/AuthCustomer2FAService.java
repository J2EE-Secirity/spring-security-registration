package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AuthUser2FA;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.AuthCustomer2FARepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.totp.util.TOTPCustomUtils;
import com.mycompany.myapp.service.dto.AuthCode2FA;
import org.apache.commons.codec.DecoderException;
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
    private AuthCustomer2FARepository customer2faRepository;

    public String getTokenAuthentication(AuthUser2FA user2fa) throws DecoderException, GeneralSecurityException {
        Optional<User> user = findByUser(user2fa);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            String secret = TOTPCustomUtils.generateSecret();
            user2fa.setSecretKey(secret);
            customer2faRepository.findByToken(token, user2fa);
            customer2faRepository.findByLogin(user2fa.getLogin(), user2fa);
            logger.debug(">>> to You send a verification CODE: {}", TOTPCustomUtils.getRfcOTPCode(secret));
            return token;
        }
        return null;
    }

    public boolean getUserConfirmAuthentication(String token, AuthCode2FA code) throws DecoderException, GeneralSecurityException {
        AuthUser2FA user = findByToken(token);
        if (user != null
            && TOTPCustomUtils.checkCode(user.getSecretKey(), code.getVerficationCode())) {
            customer2faRepository.findByCode(code.getVerficationCode(), user);
            return true;
        }
        return false;
    }

    public Optional<User> findByUser(AuthUser2FA user2fa) {
        logger.debug("> Step #1: to do user authentication by email & password");

        Optional<User> user = repository.findOneWithAuthoritiesByEmail(user2fa.getLogin());
        if (user.isPresent()
            && passwordEncoder.matches(user2fa.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    public AuthUser2FA findByToken(String token) {
        logger.debug("> Step #2: get successfully authentication to user by token from cache");

        return customer2faRepository.findByToken(token, null);
    }

    public AuthUser2FA findByLogin(String login) {
        logger.debug("> Step #2: get authorization to user by login from cache");

        return customer2faRepository.findByLogin(login, null);
    }

    public AuthUser2FA findByCode(long code) {
        logger.debug("> Step #3: get authorization to user by code from cache");

        return customer2faRepository.findByCode(code, null);
    }

}

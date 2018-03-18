package org.baeldung.service;

import org.apache.commons.codec.DecoderException;
import org.baeldung.persistence.dao.Authenticated;
import org.baeldung.persistence.dao.UserRepository;
import org.baeldung.persistence.model.User;
import org.baeldung.security.totp.util.TOTPCustomUtils;
import org.baeldung.web.dto.AuthCodeDTO;
import org.baeldung.web.dto.AuthUserDTO;
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
    private Authenticated authenticated;

    public String authentication(AuthUserDTO authUserDTO) throws DecoderException, GeneralSecurityException {
        logger.info("> Step #1: to do user authentication by login & password");

        User user = findByAuthUser(authUserDTO);
        if (user!=null) {
            String token = UUID.randomUUID().toString();
            String secret = TOTPCustomUtils.generateSecret();
            authUserDTO.setSecret(secret);
            authenticated.findByToken(token, authUserDTO);
            logger.info("> CODE: {}", TOTPCustomUtils.getRfcOTPCode(secret));
            return token;
        }
        return null;
    }

    public AuthUserDTO authenticated(String token) {
        logger.info("> Step #2: get successfully authentication of user from cache");
        return authenticated.findByToken(token, null);
    }

    public AuthUserDTO authenticated2fa(String token, AuthCodeDTO authCodeDTO) throws DecoderException, GeneralSecurityException {
        AuthUserDTO authUserDTO = authenticated(token);
        if (authUserDTO !=null) {
            if (TOTPCustomUtils.checkCode(authUserDTO.getSecret(), authCodeDTO.getCode())) {
                return authUserDTO;
            }
        }
        return null;
    }

    private User findByAuthUser(AuthUserDTO authUserDTO) {
        User user = repository.findByEmail(authUserDTO.getLogin());

        if (user!=null
                && passwordEncoder.matches(authUserDTO.getPassword(), user.getPassword())) {
            return user;
        }
        return null;
    }
}

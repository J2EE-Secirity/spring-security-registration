package com.mycompany.myapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.mycompany.myapp.domain.AuthUser2FA;
import com.mycompany.myapp.service.AuthCustomer2FAService;
import com.mycompany.myapp.service.dto.AuthCode2FA;
import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.GeneralSecurityException;

@RestController
@RequestMapping("/api")
public class AuthCustomer2FAResource {

    private final Logger logger = LoggerFactory.getLogger(AuthCustomer2FAResource.class);

    /**
     * 2FA Cache Name
     */
    private static final String TOKEN_2FA_NAME = "2FA-AUTH-TOKEN";
    /**
     * Cache Expire Time
     * <sec.> * <min.>
     */
    private static final int TOKEN_2FA_EXPIRE = 60 * 2;

    @Autowired
    private AuthCustomer2FAService service;

    @PostMapping(value = "/authentication")
    @Timed
    public ResponseEntity<String> authentication(@Validated @RequestBody AuthUser2FA user,
                                                 BindingResult binding,
                                                 HttpServletResponse response) {
        if (!binding.hasErrors()) {
            try {
                response.addCookie(newCookie(user));
                return new ResponseEntity<>("The verification code was sent to You on " + user.getNotifyBy(), HttpStatus.OK);
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
            }
            return new ResponseEntity<>("Authentication failed!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/confirm-authentication")
    @Timed
    public ResponseEntity<String> confirmAuthentication(@CookieValue(value = TOKEN_2FA_NAME) String token,
                                                        @RequestBody AuthCode2FA code,
                                                        BindingResult binding) {
        if (!binding.hasErrors()) {
            if (!StringUtils.isEmpty(token)) {
                try {
                    return service.getUserConfirmAuthentication(token, code)
                        ? new ResponseEntity<>("You have successfully authenticated!", HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.FORBIDDEN);
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                }
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private Cookie newCookie(AuthUser2FA user) throws DecoderException, GeneralSecurityException, BadCredentialsException {
        String token = service.getTokenAuthentication(user);
        if (!StringUtils.isEmpty(token)) {
            Cookie cookie = new Cookie(TOKEN_2FA_NAME, token);
            cookie.setMaxAge(TOKEN_2FA_EXPIRE);
            return cookie;
        }
        throw new BadCredentialsException("Invalid verfication");
    }
}

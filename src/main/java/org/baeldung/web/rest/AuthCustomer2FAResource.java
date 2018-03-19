package org.baeldung.web.rest;

import org.apache.commons.codec.DecoderException;
import org.baeldung.persistence.model.AuthUser2FA;
import org.baeldung.service.AuthCustomer2FAService;
import org.baeldung.web.dto.AuthCode2FA;
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

/**
 * @see https://stackoverflow.com/questions/41861164/how-can-i-manually-describe-an-example-input-for-a-java-requestbody-mapstring
 *      https://github.com/springfox/springfox/issues/1503
 *      https://github.com/springfox/springfox/issues/1345
 *      https://github.com/springfox/springfox/issues/1342
 * https://g00glen00b.be/documenting-rest-api-swagger-springfox
 * @see https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/cookie-handling.html
 */

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
    public ResponseEntity<String> authentication(
//            @ApiParam(required = true,
//                    name = "authUser2FA",
//                    value = "authUser2FA",
//                    defaultValue = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}",
//                    example = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}",
//                    examples = @Example({
//                        @ExampleProperty(mediaType = "application/json",
//                                value = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}")}))
            @Validated @RequestBody AuthUser2FA user,
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
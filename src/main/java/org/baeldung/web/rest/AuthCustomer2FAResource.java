package org.baeldung.web.rest;

import org.apache.commons.codec.DecoderException;
import org.baeldung.service.AuthCustomer2FAService;
import org.baeldung.web.dto.AuthCode;
import org.baeldung.persistence.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final String COOKIE_TOKEN_2FA = "token";

    @Autowired
    private AuthCustomer2FAService authCustomer2FAService;

    @PostMapping(value = "/authentication")
    public ResponseEntity<String> authentication(
//            @ApiParam(required = true,
//                    name = "authUser",
//                    value = "authUser",
//                    defaultValue = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}",
//                    example = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}",
//                    examples = @Example({
//                        @ExampleProperty(mediaType = "application/json",
//                                value = "{\"login\":\"test@test.com\",\"password\":\"test\",\"notifyBy\":\"GOOGLE_EMAIL\"}")}))
                @Validated @RequestBody AuthUser authUser,
                BindingResult bindingResult,
                HttpServletResponse response) {
        if (!bindingResult.hasErrors()) {
            String token = null;
            try {
                token = authCustomer2FAService.authentication(authUser);
                if (token!=null) {
                    response.addCookie(newCookie(COOKIE_TOKEN_2FA, token));
                    return new ResponseEntity<>("The verification code was sent to You on " + authUser.getNotifyBy(), HttpStatus.OK);
                }
            } catch (DecoderException|GeneralSecurityException e) {
            }
            return new ResponseEntity<>("Authentication failed!", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(value = "/authenticated")
    public ResponseEntity<AuthUser> authenticated(@CookieValue(value = COOKIE_TOKEN_2FA) String token,
                                                  @RequestBody AuthCode authCode,
                                                  BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            if (token!=null) {
                try {
                    AuthUser authUser = authCustomer2FAService.authenticated2fa(token, authCode);
                    return authUser != null
                            ? new ResponseEntity<>(authUser, HttpStatus.OK)
                            : new ResponseEntity<>(HttpStatus.FORBIDDEN);
                } catch (DecoderException|GeneralSecurityException e) {
                }
            }
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private Cookie newCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60);
        return cookie;
    }
}

package com.mycompany.myapp.security.cusomer2fa;

import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.AuthCustomer2FAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Optional;

public class Custom2FAAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthCustomer2FAService authCustomer2FAService;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        final Optional<User> user = userRepository.findOneByEmailIgnoreCase(auth.getName());
        if (!user.isPresent()) {
            throw new BadCredentialsException("Invalid username or password");
        }
        /**
         * The case to 2FA Verification
         */
        if (((Custom2FAWebAuthenticationDetails) auth.getDetails()).getVerificationCode() == null) {
            throw new BadCredentialsException("Invalid verfication code");
        }

        final Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

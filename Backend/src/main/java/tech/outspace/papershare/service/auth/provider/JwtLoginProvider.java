package tech.outspace.papershare.service.auth.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import tech.outspace.papershare.service.auth.AuthProviderService;
import tech.outspace.papershare.utils.encrypt.PassEncoder;

@Slf4j
@Service
public class JwtLoginProvider extends AbstractUserDetailsAuthenticationProvider {
    private final AuthProviderService authProviderService;

    public JwtLoginProvider(AuthProviderService authProviderService) {
        this.authProviderService = authProviderService;
    }

    @Override
    protected void additionalAuthenticationChecks(
            UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        if (authentication.getPrincipal() == null) {
            log.debug("Authentication failed: No credentials attached");
            throw new BadCredentialsException("Bad Credentials");
        }

        String requestPassword = authentication.getCredentials().toString();
        if (!PassEncoder.instance().matches(requestPassword, userDetails.getPassword())) {
            log.debug("Authentication failed: Wrong password");
            throw new BadCredentialsException("Bad Credentials");
        }

        if (!(userDetails.isAccountNonLocked())) {
            log.debug("Authentication failed: Account baned");
            throw new LockedException("User Account baned");
        }
    }

    @Override
    protected UserDetails retrieveUser(
            String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        try {
            return authProviderService.loadUserByUsername(username);
        } catch (UsernameNotFoundException ex) {
            log.debug("Authentication failed: No user linked to given email");
            throw ex;
        }
    }
}

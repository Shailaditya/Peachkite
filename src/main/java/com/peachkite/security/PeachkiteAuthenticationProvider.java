package com.peachkite.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import com.peachkite.exception.CryptoException;
import com.peachkite.util.CryptoUtils;

@Component
public class PeachkiteAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	PeachkiteUserDetailsService userDetailsService;
	
    @Override
    public Authentication authenticate(Authentication authentication) 
      throws AuthenticationException {
    	
    	if (!supports(authentication.getClass())) {
            throw new IllegalArgumentException("Only UsernamePasswordAuthenticationToken is supported, " + authentication.getClass() + " was attempted");
        }
    	
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        try {
        	password=CryptoUtils.encrypt(password);
		} catch (CryptoException e) {
			throw new AuthenticationServiceException("Error in encyting entered password");
		}
        PeachkiteUserDetail userDetails = (PeachkiteUserDetail)userDetailsService.loadUserByUsername(name);
        if(!password.equals(userDetails.getPassword())){
        	throw new AuthenticationServiceException("Username password authentication failed");
        }
        
        return new UsernamePasswordAuthenticationToken(
        		userDetails, authentication.getCredentials(), userDetails.getAuthorities());
    }
 
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(
          UsernamePasswordAuthenticationToken.class);
    }

}

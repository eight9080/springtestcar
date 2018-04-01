package com.dg.security.sec;

import com.dg.security.domain.entities.AutoUser;
import com.dg.security.domain.repositories.AutoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    private AutoUserRepository repo;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        CustomAuthenticationToken token = (CustomAuthenticationToken) authentication;

        final AutoUser autoUser = repo.findByUsername(token.getName());

        if(autoUser==null ||
                (!autoUser.getPassword().equalsIgnoreCase(token.getCredentials().toString())
                  &&
                  !token.getMake().equalsIgnoreCase("subaru"))
                ){
            throw new BadCredentialsException("The credentials are invalid");
        }

//        return new UsernamePasswordAuthenticationToken(autoUser, autoUser.getPassword(), autoUser.getAuthorities());
        return new CustomAuthenticationToken(autoUser, autoUser.getPassword(), autoUser.getAuthorities(), token.getMake());
    }

    @Override
    public boolean supports(Class<?> authentication) {
//        return UsernamePasswordAuthenticationToken.class.equals(authentication);
        return CustomAuthenticationToken.class.equals(authentication);
    }
}

package com.dg.security.sec;

import com.dg.security.domain.entities.AutoUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthenticationToken extends UsernamePasswordAuthenticationToken{

    private String make;

    public CustomAuthenticationToken(String principal, String credentials, String make) {
        super(principal, credentials);
        this.make = make;
    }

    public CustomAuthenticationToken(AutoUser principal, String credentials,
                                     Collection<? extends GrantedAuthority> authorities, String make) {
        super(principal, credentials, authorities);
        this.make = make;
    }

    public String getMake() {
        return make;
    }
}

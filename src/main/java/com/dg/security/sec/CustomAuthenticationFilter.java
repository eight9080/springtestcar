package com.dg.security.sec;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String username = super.obtainUsername(request);
        final String password = super.obtainPassword(request);
        final String make = request.getParameter("make");

        final CustomAuthenticationToken token = new CustomAuthenticationToken(username, password, make);

        super.setDetails(request, token);

        return super.getAuthenticationManager().authenticate(token);
    }
}

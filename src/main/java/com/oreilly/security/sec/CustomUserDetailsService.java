package com.oreilly.security.sec;

import com.oreilly.security.domain.entities.AutoUser;
import com.oreilly.security.domain.repositories.AutoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component
public class CustomUserDetailsService  implements UserDetailsService{

    @Autowired
    private AutoUserRepository repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final AutoUser autoUser = repo.findByUsername(username);

//        final User user = new User(autoUser.getUsername(), autoUser.getPassword(),
//                AuthorityUtils.createAuthorityList(autoUser.getRole()));

        return autoUser;
    }
}

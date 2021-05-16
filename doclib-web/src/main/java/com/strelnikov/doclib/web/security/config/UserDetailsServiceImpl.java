package com.strelnikov.doclib.web.security.config;

import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.repository.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDao clientDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Client client = clientDao.findBylogin(username);
        if (client == null) {
            throw new UsernameNotFoundException("User not found");
        }
        User user = new User(client.getLogin(),
                client.getPassword(),
                client.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName().toString()))
                        .collect(Collectors.toList()));
        System.out.println(user.getAuthorities());
        return user;
    }

}

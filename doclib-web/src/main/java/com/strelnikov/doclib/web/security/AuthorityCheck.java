package com.strelnikov.doclib.web.security;

import com.strelnikov.doclib.model.roles.AuthorityType;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthorityCheck {
    public static boolean hasAuthority (Collection<? extends GrantedAuthority> authorities, AuthorityType authorityType){
        for (GrantedAuthority authority:authorities){
            if (authority.getAuthority().equals(authorityType.toString())){
                return true;
            }
        }
        return false;
    }
}

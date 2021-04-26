package com.strelnikov.doclib.model.roles;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Client {

    private String login;

    private String password;

    private String email;

    private Role role;
}

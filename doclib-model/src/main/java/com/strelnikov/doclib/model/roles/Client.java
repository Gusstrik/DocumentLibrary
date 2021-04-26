package com.strelnikov.doclib.model.roles;

import lombok.Getter;
import lombok.Setter;

public class Client {
    @Setter
    @Getter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String email;

    @Setter
    @Getter
    private Role role;
}

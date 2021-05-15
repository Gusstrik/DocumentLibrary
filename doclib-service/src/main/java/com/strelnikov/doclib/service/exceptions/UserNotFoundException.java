package com.strelnikov.doclib.service.exceptions;

import lombok.Getter;

@Getter
public class UserNotFoundException extends Exception {
    private int clientId;

    public UserNotFoundException(int clientId){
        super("User with id = " + clientId + " not found");
        this.clientId = clientId;
    }
}

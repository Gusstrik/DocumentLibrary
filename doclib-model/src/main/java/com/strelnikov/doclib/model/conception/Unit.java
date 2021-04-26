package com.strelnikov.doclib.model.conception;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Unit {

    public Unit(String name, Type type) {
        this.name = name;
        this.type = type;
    }


    private String name;

    private Type type;

    private ArrayList<Permission> permissions = new ArrayList();

    public void addPermissionsForNewUser(String login, Credentials... credentials) {
        permissions.add(new Permission(login, credentials));
    }

    private int getUserIndex(String login) {
        int index = 0;
        while (permissions.get(index).getLogin() != login && index < permissions.size()) {
            index++;
        }
        if (index==permissions.size()){
            return -1;
        }else{
            return index;
        }
    }

    public void addPermissionsForOldUser(String login, Credentials... credentials) {
        int index = getUserIndex(login);
        permissions.get(index).addCredentials(credentials);
    }
}

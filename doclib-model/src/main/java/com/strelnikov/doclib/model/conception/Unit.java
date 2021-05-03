package com.strelnikov.doclib.model.conception;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

@Getter
@Setter
public abstract class Unit {

    public Unit(){}

    public Unit(String name, UnitType unitType) {
        this.name = name;
        this.unitType = unitType;
    }

    private String name;

    private UnitType unitType;

    //private ArrayList<Permission> permissions = new ArrayList();

//    public void addPermissionsForNewUser(String login, Credentials... credentials) {
//        permissions.add(new Permission(login, credentials));
//    }
//
//    private int getUserIndex(String login) {
//        int index = 0;
//        while (permissions.get(index).getLogin() != login && index < permissions.size()) {
//            index++;
//        }
//        if (index==permissions.size()){
//            return -1;
//        }else{
//            return index;
//        }
//    }
//
//    public void addPermissionsForOldUser(String login, Credentials... credentials) {
//        int index = getUserIndex(login);
//        permissions.get(index).addCredentials(credentials);
//    }
}

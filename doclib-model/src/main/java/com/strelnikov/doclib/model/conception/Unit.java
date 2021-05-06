package com.strelnikov.doclib.model.conception;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter

@MappedSuperclass
public abstract class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column (name="catalog_id")
    private int catalogId;

    @Column(name="name",nullable = false)
    private String name;

    @Transient
    private UnitType unitType;

    public Unit(){}

    @Override
    public boolean equals(Object o){
        if (o instanceof Unit){
            Unit compare = (Unit) o;
            return compare.getId() == this.id;
        }else {
            return false;
        }
    }



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

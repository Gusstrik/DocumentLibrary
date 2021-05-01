package com.strelnikov.doclib.model.conception;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Permission {

    private String login;

    private ArrayList<Credentials> credentials;

    public Permission(String login, Credentials... credentials) {
        this.login = login;
        this.credentials = new ArrayList();
        for (Credentials cred : credentials) {
            this.credentials.add(cred);
        }
    }

    public void addCredentials(Credentials... credentials) {
        for (Credentials cred : credentials) {
            if (!this.credentials.contains(cred)) {
                this.credentials.add(cred);
            }
        }
    }
    public void removeCredentials(Credentials... credentials) {
        for (Credentials cred : credentials) {
            if (!this.credentials.contains(cred)) {
                this.credentials.remove(cred);
            }
        }
    }

}
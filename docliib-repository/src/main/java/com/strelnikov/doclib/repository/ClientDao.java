package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.roles.Client;

public interface ClientDao {

    Client findBylogin(String login);

    Client create (Client client);

    void delete (Client client);
}

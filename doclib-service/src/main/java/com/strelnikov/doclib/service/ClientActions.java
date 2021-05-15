package com.strelnikov.doclib.service;


import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.service.exceptions.UserNotFoundException;

public interface ClientActions {

    ClientDto loadClient (int id) throws UserNotFoundException;

    ClientDto saveClient(ClientDto clientDto);

    void deleteClient(int id) throws UserNotFoundException;

}

package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.service.ClientActions;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import com.strelnikov.doclib.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClientImpl implements ClientActions {

    @Autowired
    private ClientDao clientDao;

    @Autowired
    private DtoMapper dtoMapper;

    @Override
    public ClientDto loadClient(int id) throws UserNotFoundException {
        Client client = clientDao.findById(id);
        if (client == null){
            throw new UserNotFoundException(id);
        }
        return dtoMapper.mapClient(client);
    }

    private ClientDto createNewClient(ClientDto clientDto){
        Client client = dtoMapper.mapClient(clientDto);
        clientDao.create(client);
        List<Permission> permissionList = new ArrayList<>();
        return dtoMapper.mapClient(client);
    }

    @Override
    public ClientDto saveClient(ClientDto clientDto) {
        try {
            ClientDto oldClient = loadClient(clientDto.getId());
        } catch (UserNotFoundException e) {
           return createNewClient(clientDto);
        }
        return clientDto;
    }

    @Override
    public void deleteClient(int id) {

    }
}

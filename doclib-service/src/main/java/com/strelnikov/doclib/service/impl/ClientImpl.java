package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
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
    private PermissionDao permissionDao;

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
        client = clientDao.create(client);
        permissionDao.addClientToSecureTables(client);
        for (PermissionDto permissionDto:clientDto.getPermissionDtoList()){
            Permission permission = dtoMapper.mapPermission(permissionDto);
            permissionDao.updatePermission(permission.getSecuredObject(),client,
                    PermissionType.convertToInt(permission.getPermissionList()));
        }
        return dtoMapper.mapClient(client);
    }

    private ClientDto updateClient(ClientDto clientDto){
        Client client = dtoMapper.mapClient(clientDto);
        client= clientDao.updateClient(client);
        for (PermissionDto permissionDto:clientDto.getPermissionDtoList()){
            Permission permission = dtoMapper.mapPermission(permissionDto);
            permissionDao.updatePermission(permission.getSecuredObject(),client,
                    PermissionType.convertToInt(permission.getPermissionList()));
        }
        return dtoMapper.mapClient(client);
    }

    @Override
    public ClientDto saveClient(ClientDto clientDto) {
        try {
            loadClient(clientDto.getId());
            return updateClient(clientDto);
        } catch (UserNotFoundException e) {
           return createNewClient(clientDto);
        }
    }

    @Override
    public void deleteClient(int id) throws UserNotFoundException {
        ClientDto clientDto = loadClient(id);
        clientDao.delete(dtoMapper.mapClient(clientDto));
    }

    @Override
    public ClientDto loadClient(String login) throws UserNotFoundException {
        Client client = clientDao.findBylogin(login);
        if (client==null){
            throw new UserNotFoundException(0);
        }
        return dtoMapper.mapClient(client);
    }
}

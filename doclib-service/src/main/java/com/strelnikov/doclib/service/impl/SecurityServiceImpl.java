package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.SecuredObject;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.service.SecurityService;
import com.strelnikov.doclib.service.dtomapper.DtoClassMapper;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final PermissionDao permissionDao;
    private final DtoClassMapper dtoClassMapper;
    private final ClientDao clientDao;
    private final DtoMapper dtoMapper;

    public SecurityServiceImpl(@Autowired PermissionDao permissionDao, @Autowired DtoClassMapper dtoClassMapper,
                               @Autowired ClientDao clientDao, @Autowired DtoMapper dtoMapper){
        this.clientDao = clientDao;
        this.dtoMapper = dtoMapper;
        this.dtoClassMapper = dtoClassMapper;
        this.permissionDao =permissionDao;
    }


    @Override
    public boolean checkPermission(Object object, String login, PermissionType permissionType) {
        SecuredObject securedObject = dtoClassMapper.mapClass(object);
        return permissionDao.checkPermission(securedObject, clientDao.findBylogin(login), permissionType);

    }

    @Override
    public List<PermissionDto> getObjectPermissions(Object object) {
        SecuredObject securedObject = dtoClassMapper.mapClass(object);
        List<Permission> permissionList = permissionDao.getPermissionsOfSecuredObject(securedObject);
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        for (Permission permission:permissionList){
            permissionDtoList.add(dtoMapper.mapPermission(permission));
        }
        return permissionDtoList;
    }

    @Override
    public void inheritPermissions(SecuredObject heir, SecuredObject parent) {
        List<Permission> permissionList = permissionDao.getPermissionsOfSecuredObject(parent);
        for (Permission permission : permissionList){
            permissionDao.updatePermission(heir,permission.getClient(),PermissionType.convertToInt(permission.getPermissionList()));
        }
    }

    @Override
    public void addObjectToSecureTable(SecuredObject securedObject) {
        permissionDao.addObjectToSecureTables(securedObject);
    }

    @Override
    public void removeObjectFromSecureTable(SecuredObject securedObject) {
        permissionDao.removeObjectFromSecureTables(securedObject);
    }

    @Override
    public void addClientToSecureTable(Client client) {
        permissionDao.addClientToSecureTables(client);
    }

    @Override
    public void updatePermissions(SecuredObject securedObject, Client client, List<PermissionType> permissionTypeList) {
        permissionDao.updatePermission(securedObject,client,PermissionType.convertToInt(permissionTypeList));
    }

    @Override
    public void updatePermissions(List<PermissionDto> permissionDtoList) {
        for(PermissionDto permissionDto:permissionDtoList){
            Permission permission = dtoMapper.mapPermission(permissionDto);
            permissionDao.updatePermission(permission.getSecuredObject(),permission.getClient(),PermissionType.convertToInt(permission.getPermissionList()));
        }
    }

    @Override
    public List<SecuredObject> filterList(List<SecuredObject> securedObjectList, String login, PermissionType permissionType) {
        Client client = clientDao.findBylogin(login);
        List<SecuredObject> newSecuredObjectList = new ArrayList<>();
        for (SecuredObject securedObject:securedObjectList){
            if(permissionDao.checkPermission(securedObject,client,permissionType)){
                newSecuredObjectList.add(securedObject);
            }
        }
        return newSecuredObjectList;
    }


}

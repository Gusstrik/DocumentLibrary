package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.roles.SecuredObject;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.dtomapper.DtoClassMapper;
import com.strelnikov.doclib.service.dtomapper.DtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityImpl implements SecurityActions {
    @Autowired
     private PermissionDao checkPermission;

    @Autowired
    private DtoClassMapper dtoClassMapper;

    @Autowired
    private ClientDao client;

    @Autowired
    private DtoMapper dtoMapper;

    @Override
    public boolean checkPermission(Object object, String login, PermissionType permissionType) {
        SecuredObject securedObject = dtoClassMapper.mapClass(object);
        return checkPermission.checkPermission(securedObject, client.findBylogin(login), permissionType);

    }

    @Override
    public List<PermissionDto> getObjectPermissions(Object object) {
        SecuredObject securedObject = dtoClassMapper.mapClass(object);
        List<Permission> permissionList = checkPermission.getPermissionsOfSecuredObject(securedObject);
        List<PermissionDto> permissionDtoList = new ArrayList<>();
        for (Permission permission:permissionList){
            permissionDtoList.add(dtoMapper.mapPermission(permission));
        }
        return permissionDtoList;
    }

    @Override
    public void inheritPermissions(Object heir, CatalogDto parent) {
        SecuredObject securedHeir = dtoClassMapper.mapClass(heir);
        Catalog secureParent = dtoMapper.mapCatalog(parent);
        List<Permission> permissionList = checkPermission.getPermissionsOfSecuredObject(secureParent);
        for (Permission permission : permissionList){
            checkPermission.updatePermission(securedHeir,permission.getClient(),PermissionType.convertToInt(permission.getPermissionList()));
        }
    }


}

package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.SecuredObject;

import java.util.List;

public interface SecurityActions {

    boolean checkPermission (Object object, String login, PermissionType permissionType);

    List<PermissionDto> getObjectPermissions (Object object);

    void inheritPermissions (SecuredObject heir, SecuredObject parent);

    void addObjectToSecureTable(SecuredObject securedObject);

    void removeObjectFromSecureTable(SecuredObject securedObject);

    void addClientToSecureTable(Client client);

    void updatePermissions (SecuredObject securedObject, Client client, List<PermissionType> permissionTypeList);
}

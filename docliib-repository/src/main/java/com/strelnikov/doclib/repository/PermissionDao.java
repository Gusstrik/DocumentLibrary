package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.SecuredObject;

import java.util.List;

public interface PermissionDao {

    Integer getClassId(Class clazz);

    boolean checkPermission (SecuredObject securedObject, Client client, PermissionType permissionType);

    Integer getObjectSecureId(SecuredObject securedObject);

    SecuredObject getSecuredObjectByObjectName(String objectName, String type);

    List<Permission> getClientPermissions(Client client);

    List<Permission> getPermissionsOfSecuredObject(SecuredObject securedObject);

    SecuredObject getSecuredObjectBySecureId(int id);

    void updatePermission(SecuredObject securedObject, Client client, int permission);

    void addClientToSecureTables(Client client);
}

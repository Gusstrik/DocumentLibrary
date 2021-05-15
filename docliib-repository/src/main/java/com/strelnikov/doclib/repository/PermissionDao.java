package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.roles.Permission;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.Client;
import com.strelnikov.doclib.model.roles.SecuredObject;

import java.util.List;

public interface PermissionDao {

    Integer getClassId(Class clazz);

    Integer getSecuredObjectId(int objectId, Class clazz);

    boolean checkPermission (int objectId, Class clazz, Client client, PermissionType permissionType);

    List<Permission> getClientPermissions(Client client);

    List<Permission> getPermissionsByObj(SecuredObject securedObject);
}

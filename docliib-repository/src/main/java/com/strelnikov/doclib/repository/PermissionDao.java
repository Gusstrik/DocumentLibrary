package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.conception.Permission;
import com.strelnikov.doclib.model.roles.Client;

import java.util.List;

public interface PermissionDao {

    Integer getClassId(Class clazz);

    Integer getSecuredObjectId(int objectId, Class clazz);

    boolean checkPermission (int objectId, Class clazz, Client client, Permission permission);

    List<Permission> getPermissions (int objectId, Class clazz, Client client);
}

package com.strelnikov.doclib.repository;

import com.strelnikov.doclib.model.conception.Permission;
import com.strelnikov.doclib.model.roles.Client;

public interface ICheckPermission {

    Integer getClassId(Class clazz);

    Integer getSecuredObjectId(int objectId, Class clazz);

    boolean checkPermission (int objectId, Class clazz, Client client, Permission permission);
}

package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.roles.PermissionType;

public interface SecurityActions {

    boolean checkPermission (int id,Object object, String login, PermissionType permissionType);
}

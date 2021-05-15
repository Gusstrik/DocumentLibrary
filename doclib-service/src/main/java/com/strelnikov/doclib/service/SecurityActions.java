package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.roles.SecuredObject;

public interface SecurityActions {

    boolean checkPermission (SecuredObject securedObject, String login, PermissionType permissionType);
}

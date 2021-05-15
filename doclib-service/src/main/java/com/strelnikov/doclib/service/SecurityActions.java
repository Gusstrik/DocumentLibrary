package com.strelnikov.doclib.service;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.PermissionType;

import java.util.List;

public interface SecurityActions {

    boolean checkPermission (Object object, String login, PermissionType permissionType);

    List<PermissionDto> getObjectPermissions (Object object);

    void inheritPermissions (Object heir, CatalogDto parent);
}

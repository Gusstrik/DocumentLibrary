package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
import com.strelnikov.doclib.model.roles.SecuredObject;
import com.strelnikov.doclib.repository.ClientDao;
import com.strelnikov.doclib.repository.PermissionDao;
import com.strelnikov.doclib.service.SecurityActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityImpl implements SecurityActions {
    @Autowired
     private PermissionDao checkPermission;

    @Autowired
    private ClientDao client;

    @Override
    public boolean checkPermission(SecuredObject securedObject, String login, PermissionType permissionType) {
        return checkPermission.checkPermission(securedObject, client.findBylogin(login), permissionType);

    }
}

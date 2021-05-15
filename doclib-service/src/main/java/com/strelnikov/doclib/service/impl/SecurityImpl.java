package com.strelnikov.doclib.service.impl;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Permission;
import com.strelnikov.doclib.model.documnets.Document;
import com.strelnikov.doclib.model.documnets.DocumentFile;
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
    public boolean checkPermission(int id, Object object, String login, Permission permission) {
        Class clazz=null;
        switch (object.getClass().getSimpleName()){
            case "CatalogDto":
                clazz = Catalog.class;
                break;
            case "DocumentDto":
                clazz = Document.class;
                break;
            case "DocFileDto":
                clazz = DocumentFile.class;
                break;
        }
        if (clazz!=null) {
            return checkPermission.checkPermission(id, clazz, client.findBylogin(login), permission);
        }else {
            return false;
        }
    }
}

package com.strelnikov.doclib.service;

import com.strelnikov.doclib.model.conception.Permission;

public interface SecurityActions {

    boolean checkPermission (int id,Object object, String login, Permission permission);
}

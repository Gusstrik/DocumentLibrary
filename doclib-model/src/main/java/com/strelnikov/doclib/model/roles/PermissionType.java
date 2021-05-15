package com.strelnikov.doclib.model.roles;

import java.util.List;

public enum PermissionType {
    READING,
    WRITING,
    MODERATING;

    public boolean check(int permissionNum){
        int result = 0;
        switch (this){
            case READING:
                result = permissionNum & 1;
                break;
            case WRITING:
                result = permissionNum & 2;
                break;
            case MODERATING:
                result = permissionNum & 4;
                break;
        }
        if (result!=0) {
            return true;
        }
        return false;
    }

    public static int convertToInt(List<PermissionType> permissionTypeList){
        int result = 0;
        if (permissionTypeList.contains(PermissionType.READING)){
            result = result | 1;
        }
        if (permissionTypeList.contains(PermissionType.WRITING)){
            result = result | 2;
        }
        if (permissionTypeList.contains(PermissionType.MODERATING)){
            result = result | 4;
        }
        return result;
    }
}

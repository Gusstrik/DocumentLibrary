package com.strelnikov.doclib.model.conception;

public enum Permission {
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
}

package com.strelnikov.doclib.web.security.config;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component("permissionEvaluator")
public class PermissionEvaluator implements org.springframework.security.access.PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if (authentication != null && permission instanceof String) {
            if(authentication.getAuthorities().contains("ROLE_ADMIN")){
                return true;
            }
            User loggedUser = (User) authentication.getPrincipal();
            String permissionToCheck = (String) permission;
            // in this part of the code you need to check if the loggedUser has the "permission" over the
            // targetDomainObject. In this implementation the "permission" is a string, for example "read", or "update"
            // The targetDomainObject is an actual object, for example a object of UserProfile class (a class that
            // has the profile information for a User)

            // You can implement the permission to check over the targetDomainObject in the way that suits you best
            // A naive approach:
//            if (targetDomainObject.getClass().getSimpleName().compareTo("UserProfile") == 0) {
//                if ((UserProfile) targetDomainObject.getId() == loggedUser.getId())
//                    return true;
//            }
            // A more robust approach: you can have a table in your database holding permissions to each user over
            // certain targetDomainObjects
//            List<Permission> userPermissions = permissionRepository.findByUserAndObject(loggedUser,
//                    targetDomainObject.getClass().getSimpleName());
            // now check if in userPermissions list we have the "permission" permission.

            // ETC...
        }
        //access denied
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}

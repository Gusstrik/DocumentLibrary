package com.strelnikov.doclib.model.roles;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class Permission {

    private int clientId;

    SecuredObject securedObject;

    private List<PermissionType> permissionList;
}

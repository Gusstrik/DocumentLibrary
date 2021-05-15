package com.strelnikov.doclib.dto;

import com.strelnikov.doclib.model.roles.PermissionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto {

    private int clientId;

    private int ObjectId;

    private List<PermissionType> permissionTypeList;

}

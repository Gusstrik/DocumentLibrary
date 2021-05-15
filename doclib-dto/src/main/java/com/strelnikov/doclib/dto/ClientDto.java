package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    private int id;

    private String login;

    private String password;

    private List<PermissionDto> permissionDtoList;

}

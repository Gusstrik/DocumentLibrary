package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class UnitDto {

    private int id;
    @NonNull
    private String name;

    @NonNull
    private String unitType;

    private int parentId;
    public UnitDto(){}
}

package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnitDto {

    private int id;
    @NonNull
    private String name;

    @NonNull
    private String unitType;

    private int parentId;

}

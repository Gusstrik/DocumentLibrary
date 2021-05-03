package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


import java.util.List;

@Getter
@AllArgsConstructor
public class CatalogDto {


    private int id;
    @NonNull
    private String name;
    private int parentId;
    @NonNull
    private List<UnitDto> contentList;

}

package com.strelnikov.doclib.dto;

import lombok.*;


import java.util.List;

@Getter
@AllArgsConstructor
public class CatalogDto {

    private int id = 0;
    @NonNull

    private String name;

    private int parentId;

    @NonNull
    private List<UnitDto> contentList;

    public CatalogDto(){}
}

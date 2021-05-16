package com.strelnikov.doclib.dto;

import lombok.*;


import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CatalogDto {

    private int id;

    @NonNull
    private String name;

    private int parentId;

    @NonNull
    private List<UnitDto> contentList;

}

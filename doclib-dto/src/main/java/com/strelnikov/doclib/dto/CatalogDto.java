package com.strelnikov.doclib.dto;

import com.strelnikov.doclib.model.catalogs.Catalog;
import com.strelnikov.doclib.model.conception.Unit;
import lombok.Getter;


import java.util.List;

@Getter
public class CatalogDto {
    private String name;
    private String parent;
    private List<Unit> contentList;

    public CatalogDto(Catalog catalog){
        this.name= catalog.getName();
        this.contentList=catalog.getContentList();
    }
    public CatalogDto(String name,String parent){
        this.name= name;
        this.parent=parent;
    }
}

package com.strelnikov.doclib.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DocTypeDto {
    List<String> typeList;

    public DocTypeDto(List<String> typeList){
        this.typeList=typeList;
    }

}

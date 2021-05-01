package com.strelnikov.doclib.dto;

import lombok.Getter;

@Getter
public class DocFileDto {
    private String name;
    private String path;

    public DocFileDto(String name, String path) {
        this.name = name;
        this.path = path;
    }

}

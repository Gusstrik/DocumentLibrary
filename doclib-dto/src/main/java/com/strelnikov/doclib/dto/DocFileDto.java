package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class DocFileDto {

    private int id;

    @NonNull
    private String name;
    @NonNull
    private String path;

    public DocFileDto(){}
}

package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class DocTypeDto {

    public static List<DocTypeDto> typesList =new ArrayList<>();

    private int id;
    private String docType;

}

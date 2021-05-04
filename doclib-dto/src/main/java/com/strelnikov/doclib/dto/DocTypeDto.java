package com.strelnikov.doclib.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class DocTypeDto {

    public static List<String> typesList =new ArrayList<>();
    @NonNull
    private String docType;

}

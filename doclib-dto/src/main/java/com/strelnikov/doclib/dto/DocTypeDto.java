package com.strelnikov.doclib.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocTypeDto {

    public static List<DocTypeDto> typesList =new ArrayList<>();

    private int id;
    private String docType;

}

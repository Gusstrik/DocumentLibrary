package com.strelnikov.doclib.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocVersionDto {

    private int id;

    private int documentId;

    private int version;

    private String description;

    @NonNull
    private String importance;

    private boolean isModerated;
    @NonNull
    private List<DocFileDto>fileList;

}

package com.strelnikov.doclib.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
public class DocVersionDto {

    private int id;

    private int documentId;

    private int version;
    private String description;
    @NonNull
    private String importance;

    @JsonProperty("moderated")
    private boolean isModerated;
    @NonNull
    private List<DocFileDto>fileList;

    public DocVersionDto(){}
}

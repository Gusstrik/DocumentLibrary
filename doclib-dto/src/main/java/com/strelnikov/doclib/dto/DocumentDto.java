package com.strelnikov.doclib.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDto {

    private int id;

    @NonNull
    private String name;

    @NonNull
    private int type;

    private int actualVersion;

    private int catalogId;

    @NonNull
    private DocVersionDto version;
}

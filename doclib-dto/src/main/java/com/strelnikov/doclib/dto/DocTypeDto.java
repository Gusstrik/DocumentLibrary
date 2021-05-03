package com.strelnikov.doclib.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class DocTypeDto {
    @NonNull
    private String docType;

}

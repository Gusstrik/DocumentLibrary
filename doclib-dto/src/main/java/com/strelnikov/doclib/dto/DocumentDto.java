package com.strelnikov.doclib.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DocumentDto {
    private String name;
    private int version;
    private String type;
    private List<DocFileDto> fileList;
    private boolean isModerated;
    private String catalogName;

    public DocumentDto(String name, int version, String type){
        this.name=name;
        this.version=version;
        this.type=type;
    }

    public DocumentDto(String name, int version,String type,String parent){
        this(name, version, type);
        this.catalogName=parent;
    }

    public DocumentDto(String name, int version, String type, List<DocFileDto> fileList,boolean isModerated){
        this(name, version, type);
        this.fileList=fileList;
        this.isModerated=isModerated;
    }

    public DocumentDto(String name, int version, String type, List<DocFileDto> fileList,boolean isModerated, String catalogName){
        this(name, version, type, fileList, isModerated);
        this.catalogName = catalogName;
    }
}

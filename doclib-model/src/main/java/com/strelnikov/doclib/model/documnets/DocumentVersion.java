package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentVersion {

    private int id;

    private int documentId;

    private String description;

    private int version;

    private Importance importance;

    private List<DocumentFile> filesList = new ArrayList<>();

    private boolean isModerated;

    public DocumentVersion() {

    }

    @Override
    public boolean equals(Object o){
        if (o instanceof DocumentVersion compare){
            return compare.getId() == this.id;
        }else {
            return false;
        }
    }

}

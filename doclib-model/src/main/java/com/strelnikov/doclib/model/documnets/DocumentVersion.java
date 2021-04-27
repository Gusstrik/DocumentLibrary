package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DocumentVersion {


    private String description;

    private Importance importance;

    private List<DocumentFile> filesList = new ArrayList();

    private boolean isModerated;

    public DocumentVersion() {

    }

    public DocumentVersion(String description, boolean isModerated) {
        this.description = description;
        this.isModerated = isModerated;
    }

}

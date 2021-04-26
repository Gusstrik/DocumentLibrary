package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class DocumentVersion {

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private Importance importance;

    public ArrayList<File> filesList = new ArrayList();

    @Getter
    @Setter
    private boolean isModerated;

}

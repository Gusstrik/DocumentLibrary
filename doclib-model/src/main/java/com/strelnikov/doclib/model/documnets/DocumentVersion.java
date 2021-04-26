package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DocumentVersion {


    private String description;

    private Importance importance;

    public ArrayList<File> filesList = new ArrayList();

    private boolean isModerated;

}

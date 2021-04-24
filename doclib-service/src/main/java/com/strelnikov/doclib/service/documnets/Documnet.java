package com.strelnikov.doclib.service.documnets;

import com.strelnikov.doclib.service.conception.Entity;
import com.strelnikov.doclib.service.conception.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

public class Documnet extends Entity {
    public ArrayList<DocumentVersion> versionsList = new ArrayList();

    @Setter
    @Getter
    private int ActualVersion;

    @Setter
    @Getter
    private DocumentType documentType;

    public Documnet(String name) {
        super(name, Type.DOCUMENT);
    }
}

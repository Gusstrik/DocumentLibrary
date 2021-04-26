package com.strelnikov.doclib.model.documnets;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.Type;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class Documnet extends Unit {
    public ArrayList<DocumentVersion> versionsList = new ArrayList();

    private int ActualVersion;

    private DocumentType documentType;

    public Documnet(String name) {
        super(name, Type.DOCUMENT);
    }
}

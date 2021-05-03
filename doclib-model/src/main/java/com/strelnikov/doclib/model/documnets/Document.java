package com.strelnikov.doclib.model.documnets;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter
@Getter
public class Document extends Unit {
    public ArrayList<DocumentVersion> versionsList = new ArrayList<>();

    private int actualVersion;

    private DocumentType documentType = new DocumentType();

    public Document(){
        super();
        this.setUnitType(UnitType.DOCUMENT);
        actualVersion=0;
        versionsList.add(new DocumentVersion());
    }

    public DocumentVersion getDocumentVersion() {
        return versionsList.get(actualVersion);
    }

    public DocumentVersion getDocumentVersion(int version) {
        if (version < actualVersion) {
            return versionsList.get(version);
        } else {
            return versionsList.get(actualVersion);
        }
    }
}

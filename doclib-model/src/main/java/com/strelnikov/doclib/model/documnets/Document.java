package com.strelnikov.doclib.model.documnets;

import com.strelnikov.doclib.model.conception.Unit;
import com.strelnikov.doclib.model.conception.UnitType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter

@Entity
@Table(name = "documents")
public class Document extends Unit {

    @Transient
    public List<DocumentVersion> versionsList = new ArrayList<>();

    @Column(name = "actual_version", nullable = false)
    private int actualVersion;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinColumn(name="type_id")
    private DocumentType documentType = new DocumentType();


    public Document() {
        super();
        this.setUnitType(UnitType.DOCUMENT);
        actualVersion = 0;
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

    public boolean isVersionExist(DocumentVersion docVersion) {
        for (DocumentVersion existingVer : versionsList) {
            if (existingVer.equals(docVersion)) {
                return true;
            }
        }
        return false;
    }

}

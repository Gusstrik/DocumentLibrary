package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "documents_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY,targetEntity = Document.class)
    @JoinColumn(name="document_id")
    private int documentId;

    @Column(name="description")
    private String description;

    @Column(name="version",nullable = false)
    private int version;

    @Column(name = "importance",nullable = false)
    @Enumerated(EnumType.STRING)
    private Importance importance;

    @OneToMany(mappedBy = "docVersionId", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DocumentFile> filesList = new ArrayList<>();

    @Column(name="is_moderated",nullable = false)
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

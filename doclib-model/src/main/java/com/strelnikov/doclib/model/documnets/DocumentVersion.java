package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "documents_versions")
public class DocumentVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.EAGER,targetEntity = Document.class)
    @JoinColumn(name="document_id")
    Document parentDocument;

    @Column(name="description")
    private String description;

    @Column(name="version",nullable = false)
    private int version;

    @Column(name = "importance")
    @Enumerated(EnumType.STRING)
    private Importance importance;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "files_versions",
            joinColumns = @JoinColumn(name = "version_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private List<DocumentFile> filesList;

    @Column(name="is_moderated",nullable = false)
    private boolean isModerated;




    @Override
    public boolean equals(Object o){
        if (o instanceof DocumentVersion compare){
            return compare.getId() == this.id;
        }else {
            return false;
        }
    }

}

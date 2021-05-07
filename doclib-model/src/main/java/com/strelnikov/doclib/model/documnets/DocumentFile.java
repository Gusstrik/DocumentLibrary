package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter

@Entity
@Table(name="doc_files")
public class DocumentFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String fileName;

    @ManyToOne(cascade = CascadeType.MERGE,fetch = FetchType.LAZY,targetEntity = DocumentVersion.class)
    @JoinColumn(name="document_id")
    private DocumentVersion docVersion;

    @Column(name = "path", nullable = false)
    private String filePath;

    public DocumentFile(){

    }

    public DocumentFile(String fileName, String filePath){
        this.fileName=fileName;
        this.filePath=filePath;
    }
}

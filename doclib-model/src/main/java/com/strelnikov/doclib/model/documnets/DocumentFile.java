package com.strelnikov.doclib.model.documnets;

import com.strelnikov.doclib.model.roles.SecuredObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter

@Entity
@Table(name = "doc_files")
public class DocumentFile implements SecuredObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String fileName;

    @ManyToMany(mappedBy = "filesList")
    private List<DocumentVersion> docVersion;

    @Column(name = "path", nullable = false)
    private String filePath;

    public DocumentFile() {

    }

    public DocumentFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    @Override
    public String getName() {
        return getFileName();
    }
}

package com.strelnikov.doclib.model.documnets;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.print.Doc;
import java.util.List;

@Getter
@Setter

@Entity
@Table (name = "doc_types", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
})
public class DocumentType {
    @Transient
    public static List<DocumentType> documentTypeList;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(name = "name", nullable = false)
    private String curentType;

    public DocumentType() {}
    public DocumentType(String type) {
        curentType = type;
    }

    public boolean isTypeExist(){
        for (DocumentType docType:documentTypeList){
            if (this.curentType.equals(docType.getCurentType())){
                return true;
            }
        }
        return false;
    }
}

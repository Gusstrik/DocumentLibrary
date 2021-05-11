package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.service.DocTypeActions;
import com.strelnikov.doclib.service.exceptions.TypeIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
public class DocTypeRestController {

    @Autowired
    DocTypeActions docTypeAct;

    @GetMapping
    public ResponseEntity<List<DocTypeDto>> getTypeList(){
        docTypeAct.refreshListDocumentType();
        return ResponseEntity.ok(DocTypeDto.typesList);
    }

    @PostMapping
    public ResponseEntity<Object> postType(@RequestBody DocTypeDto docTypeDto){
        try {
            docTypeDto= docTypeAct.addDocumentType(docTypeDto);
            return ResponseEntity.ok(docTypeDto);
        } catch (TypeIsAlreadyExistException e) {
            return ResponseEntity.badRequest().body("Type is already exist");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<List<DocTypeDto>> deleteType (@PathVariable int id){
        docTypeAct.deleteDocumentType(id);
        return ResponseEntity.ok(DocTypeDto.typesList);
    }
}

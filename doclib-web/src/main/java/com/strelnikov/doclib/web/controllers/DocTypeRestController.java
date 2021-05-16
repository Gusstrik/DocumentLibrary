package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.service.DocTypeService;
import com.strelnikov.doclib.service.exceptions.TypeIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/type")

public class DocTypeRestController {

    @Autowired
    DocTypeService docTypeAct;

    @GetMapping(value = "/get",produces = "application/json")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<List<DocTypeDto>> getTypeList(){
        docTypeAct.refreshListDocumentType();
        return ResponseEntity.ok(DocTypeDto.typesList);
    }

    @PostMapping(value = "/post",consumes = "application/json",produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<DocTypeDto> postType(@RequestBody DocTypeDto docTypeDto){
        try {
            docTypeDto= docTypeAct.addDocumentType(docTypeDto);
            return ResponseEntity.ok(docTypeDto);
        } catch (TypeIsAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "delete/{id}", produces = "application/json")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<DocTypeDto>> deleteType (@PathVariable int id){
        docTypeAct.deleteDocumentType(id);
        return ResponseEntity.ok(DocTypeDto.typesList);
    }
}

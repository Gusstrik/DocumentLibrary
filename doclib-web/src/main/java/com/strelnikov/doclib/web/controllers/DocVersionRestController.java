package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/document/{id}/version")
public class DocVersionRestController {

    @Autowired
    DocVersionActions docVersionActions;

    @Autowired
    DocumentActions documentActions;

    @GetMapping
    public ResponseEntity<List<DocVersionDto>> getVersionList(@PathVariable("id") int id){
        try {
            return ResponseEntity.ok(docVersionActions.getVersionList(id));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{version}")
    public ResponseEntity<Object> getDocVersion(@PathVariable("id") int id, @PathVariable("version") int version){
        try{
            return ResponseEntity.ok(documentActions.loadDocument(id,version));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).body("Document with id"+e.getUnitId()+" not found");
        } catch (VersionNotExistException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

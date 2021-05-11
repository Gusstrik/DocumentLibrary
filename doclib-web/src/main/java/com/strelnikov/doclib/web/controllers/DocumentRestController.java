package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/document")
public class DocumentRestController {

    @Autowired
    DocumentActions docAct;

    @GetMapping("{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable int id){
        try{
            DocumentDto docDto = docAct.loadDocument(id);
            return ResponseEntity.ok(docDto);
        } catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Object> postDocument(@RequestBody DocumentDto documentDto){
        try {
            documentDto = docAct.saveDocument(documentDto);
            return ResponseEntity.ok(documentDto);
        } catch (VersionIsAlreadyExistException e) {
            return ResponseEntity.badRequest().body("Version " + e.getDocVer().getVersion()+ " of document is already exist");
        } catch (UnitIsAlreadyExistException e) {
            return ResponseEntity.badRequest().body("Document is already exist in catalog");
        } catch (FileNotFoundException e) {
            return ResponseEntity.badRequest().body("One of the files doesn't exist");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteDocument(@PathVariable int id){
        try {
            docAct.loadDocument(id);
            docAct.deleteDocument(id);
            return ResponseEntity.ok("Document was successfully deleted");
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

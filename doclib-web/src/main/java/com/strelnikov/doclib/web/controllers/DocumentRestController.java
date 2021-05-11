package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/document")
public class DocumentRestController {

    @Autowired
    DocumentActions docAct;

    @GetMapping("{id}")
    public ResponseEntity<DocumentDto> getDocuemnt(@PathVariable int id){
        try{
            DocumentDto docDto = docAct.loadDocument(id);
            return ResponseEntity.ok(docDto);
        } catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}

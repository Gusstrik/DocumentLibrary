package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
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

    @GetMapping
    public ResponseEntity<List<DocVersionDto>> getVersionList(@PathVariable("id") int id){
        try {
            return ResponseEntity.ok(docVersionActions.getVersionList(id));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

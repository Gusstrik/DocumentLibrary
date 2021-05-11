package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocTypeDto;
import com.strelnikov.doclib.service.DocTypeActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

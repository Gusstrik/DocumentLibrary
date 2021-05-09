package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping(path = "catalog")
public class CatalogRestController {

    @Autowired
    private CatalogActions catalogAct;

    @GetMapping("{id}")
    public ResponseEntity<CatalogDto> getCatalog(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            return ResponseEntity.ok(catalogDto);
        }catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }
}

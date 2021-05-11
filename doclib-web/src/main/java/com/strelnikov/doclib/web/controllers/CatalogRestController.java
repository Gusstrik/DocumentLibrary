package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.exceptions.CannotDeleteMainCatalogException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
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

    @PostMapping
    public ResponseEntity<CatalogDto> postCatalog(@RequestBody CatalogDto catalogDto){
        try{
            catalogDto = catalogAct.saveCatalog(catalogDto);
            return ResponseEntity.ok(catalogDto);
        }catch (UnitIsAlreadyExistException e) {
            ResponseEntity.badRequest().body("Catalog is already exist");
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<CatalogDto> deleteCatalog(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            catalogAct.deleteCatalog(catalogDto);
            catalogDto = catalogAct.loadCatalog(catalogDto.getParentId());
            return ResponseEntity.ok(catalogDto);
        } catch (UnitNotFoundException | CannotDeleteMainCatalogException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

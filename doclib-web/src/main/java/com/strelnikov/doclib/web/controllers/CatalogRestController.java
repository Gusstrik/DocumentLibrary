package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.exceptions.CannotDeleteMainCatalogException;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "rest/catalog")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class CatalogRestController {

    @Autowired
    private CatalogActions catalogAct;

    @Autowired
    private SecurityActions securityActions;

    @GetMapping("get/{id}")
    public ResponseEntity<CatalogDto> getCatalog(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityActions.checkPermission(id,catalogDto,auth.getName(), PermissionType.READING)) {
                return ResponseEntity.ok(catalogDto);
            }else{
                return ResponseEntity.status(403).build();
            }
        }catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("post")
    public ResponseEntity<Object> postCatalog(@RequestBody CatalogDto catalogDto){
        try{
            catalogDto = catalogAct.saveCatalog(catalogDto);
            return ResponseEntity.ok(catalogDto);
        }catch (UnitIsAlreadyExistException e) {
            return ResponseEntity.badRequest().body("Catalog is already exist");
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<Object> deleteCatalog(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            catalogAct.deleteCatalog(catalogDto);
            catalogDto = catalogAct.loadCatalog(catalogDto.getParentId());
            return ResponseEntity.ok(catalogDto);
        } catch (UnitNotFoundException e) {
            return ResponseEntity.badRequest().body("There is no catalog with id = " + id);
        }catch (CannotDeleteMainCatalogException e){
            return ResponseEntity.badRequest().body("Can't delete main catalog");
        }
    }
}

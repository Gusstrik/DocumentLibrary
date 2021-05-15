package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
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

import java.util.List;


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
            if (securityActions.checkPermission(catalogDto,auth.getName(), PermissionType.READING)) {
                return ResponseEntity.ok(catalogDto);
            }else{
                return ResponseEntity.status(403).build();
            }
        }catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("get/{id}/permissions")
    public ResponseEntity<List<PermissionDto>> getPermissionList(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityActions.checkPermission(catalogDto,auth.getName(),PermissionType.READING)) {
                return ResponseEntity.ok(securityActions.getObjectPermissions(catalogDto));
            }else{
                return ResponseEntity.status(403).build();
            }
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("post")
    public ResponseEntity<CatalogDto> postCatalog(@RequestBody CatalogDto catalogDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            if(securityActions.checkPermission(catalogAct.loadCatalog(catalogDto.getParentId()),auth.getName(),PermissionType.WRITING)){
                catalogDto = catalogAct.saveCatalog(catalogDto);
                return ResponseEntity.ok(catalogDto);
            }
            return ResponseEntity.status(403).build();
        }catch (UnitIsAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<CatalogDto> deleteCatalog(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            if(securityActions.checkPermission(catalogDto,auth.getName(),PermissionType.WRITING)){
                catalogAct.deleteCatalog(catalogDto);
                catalogDto = catalogAct.loadCatalog(catalogDto.getParentId());
                return ResponseEntity.ok(catalogDto);
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }catch (CannotDeleteMainCatalogException e){
            return ResponseEntity.badRequest().build();
        }
    }
}

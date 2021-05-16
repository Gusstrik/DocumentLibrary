package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.CatalogService;
import com.strelnikov.doclib.service.SecurityService;
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

    private final CatalogService catalogAct;
    private final SecurityService securityService;

    public CatalogRestController(@Autowired CatalogService catalogAct, @Autowired SecurityService securityService){
        this.catalogAct =catalogAct;
        this.securityService = securityService;
    }


    @GetMapping(value = "get/{id}", produces = "application/json")
    public ResponseEntity<CatalogDto> getCatalog(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityService.checkPermission(catalogDto,auth.getName(), PermissionType.READING)) {
                catalogDto = catalogAct.filterContentList(catalogDto,auth.getName(),PermissionType.READING);
                return ResponseEntity.ok(catalogDto);
            }else{
                return ResponseEntity.status(403).build();
            }
        }catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "get/{id}/permissions/get", produces = "application/json")
    public ResponseEntity<List<PermissionDto>> getPermissionList(@PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityService.checkPermission(catalogDto,auth.getName(),PermissionType.READING)) {
                return ResponseEntity.ok(securityService.getObjectPermissions(catalogDto));
            }else{
                return ResponseEntity.status(403).build();
            }
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping(value = "get/{id}/permissions/post", produces = "application/json", consumes = "application/json")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<PermissionDto>> postPermissionList(@RequestBody List<PermissionDto> permissionDtoList, @PathVariable int id){
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            securityService.updatePermissions(permissionDtoList);
            return ResponseEntity.ok(securityService.getObjectPermissions(catalogDto));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "post", produces = "application/json", consumes = "application/json")
    public ResponseEntity<CatalogDto> postCatalog(@RequestBody CatalogDto catalogDto){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            if(securityService.checkPermission(catalogAct.loadCatalog(catalogDto.getParentId()),auth.getName(),PermissionType.WRITING)){
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

    @DeleteMapping(value = "delete/{id}",produces = "application/json")
    public ResponseEntity<CatalogDto> deleteCatalog(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            CatalogDto catalogDto = catalogAct.loadCatalog(id);
            if(securityService.checkPermission(catalogDto,auth.getName(),PermissionType.WRITING)){
                catalogAct.deleteCatalog(catalogDto);
                catalogDto = catalogAct.loadCatalog(catalogDto.getParentId());
                return ResponseEntity.ok(catalogDto);
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException | CannotDeleteMainCatalogException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

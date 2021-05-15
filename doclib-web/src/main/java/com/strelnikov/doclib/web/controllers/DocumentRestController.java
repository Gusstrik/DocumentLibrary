package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.CatalogDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.CatalogActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.SecurityActions;
import com.strelnikov.doclib.service.exceptions.UnitIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("rest/document")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class DocumentRestController {

    @Autowired
    DocumentActions docAct;

    @Autowired
    SecurityActions securityActions;

    @Autowired
    CatalogActions catalogActions;

    @GetMapping("get/{id}")
    public ResponseEntity<DocumentDto> getDocument(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            DocumentDto docDto = docAct.loadDocument(id);
            if (securityActions.checkPermission(docDto,auth.getName(),PermissionType.READING)) {
                return ResponseEntity.ok(docDto);
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("post")
    public ResponseEntity<DocumentDto> postDocument(@RequestBody DocumentDto documentDto) throws VersionNotExistException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if(securityActions.checkPermission(catalogActions.loadCatalog(documentDto.getCatalogId()),auth.getName(), PermissionType.WRITING)){
                documentDto = docAct.saveDocument(documentDto);
                return ResponseEntity.ok(documentDto);
            }
            return ResponseEntity.status(403).build();
        } catch (VersionIsAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnitIsAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        } catch (FileNotFoundException e) {
            return ResponseEntity.badRequest().build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity deleteDocument(@PathVariable int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            DocumentDto documentDto = docAct.loadDocument(id);
            if (securityActions.checkPermission(documentDto,auth.getName(),PermissionType.WRITING)) {
                docAct.deleteDocument(id);
                return ResponseEntity.ok().build();
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("get/{id}/permissions/get")
    public ResponseEntity<List<PermissionDto>> getPermissionList(@PathVariable int id){
        try {
            DocumentDto documentDto = docAct.loadDocument(id);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityActions.checkPermission(documentDto,auth.getName(),PermissionType.READING)) {
                return ResponseEntity.ok(securityActions.getObjectPermissions(documentDto));
            }else{
                return ResponseEntity.status(403).build();
            }
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("get/{id}/permissions/post")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<PermissionDto>> postPermissionList(@RequestBody List<PermissionDto> permissionDtoList, @PathVariable int id){
        try {
            DocumentDto documentDto = docAct.loadDocument(id);
            securityActions.updatePermissions(permissionDtoList);
            return ResponseEntity.ok(securityActions.getObjectPermissions(documentDto));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

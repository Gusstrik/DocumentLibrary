package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.DocVersionService;
import com.strelnikov.doclib.service.DocumentService;
import com.strelnikov.doclib.service.SecurityService;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/document/get/{id}/version")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class DocVersionRestController {

    private final DocVersionService docVersionService;
    private final DocumentService documentService;
    private final SecurityService securityService;

    public DocVersionRestController(@Autowired DocVersionService docVersionService, @Autowired DocumentService documentService,
                                    @Autowired SecurityService securityService){
        this.documentService = documentService;
        this.docVersionService = docVersionService;
        this.securityService = securityService;
    }

    @GetMapping
    public ResponseEntity<List<DocVersionDto>> getVersionList(@PathVariable("id") int id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (securityService.checkPermission(documentService.loadDocument(id),auth.getName(), PermissionType.READING)) {
                return ResponseEntity.ok(docVersionService.getVersionList(id));
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{version}")
    public ResponseEntity<DocumentDto> getDocVersion(@PathVariable("id") int id, @PathVariable("version") int version){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            if (securityService.checkPermission(documentService.loadDocument(id),auth.getName(), PermissionType.READING)) {
                return ResponseEntity.ok(documentService.loadDocument(id, version));
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (VersionNotExistException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @DeleteMapping("{versionId}")
    public ResponseEntity<List<DocVersionDto>> deleteDocVersion(@PathVariable("id") int docId,@PathVariable("versionId") int versionId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (securityService.checkPermission(documentService.loadDocument(docId),auth.getName(), PermissionType.WRITING)) {
                docVersionService.deleteDocVersion(versionId);
                return ResponseEntity.ok(docVersionService.getVersionList(docId));
            }
            return ResponseEntity.status(403).build();
        } catch(UnitNotFoundException e){
                return ResponseEntity.notFound().build();
            }
    }

    @PatchMapping("/rollback/{version}")
    public ResponseEntity<List<DocVersionDto>> rollbackDoc(@PathVariable("id") int docId,@PathVariable("version") int version){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            if (securityService.checkPermission(documentService.loadDocument(docId),auth.getName(), PermissionType.WRITING)) {
                documentService.rollback(docId, version);
                return ResponseEntity.ok(docVersionService.getVersionList(docId));
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        } catch (VersionNotExistException e) {
            return  ResponseEntity.status(404).build();
        }
    }

}

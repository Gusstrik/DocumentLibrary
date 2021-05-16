package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.dto.PermissionDto;
import com.strelnikov.doclib.model.roles.PermissionType;
import com.strelnikov.doclib.service.DocFileService;
import com.strelnikov.doclib.service.SecurityService;
import com.strelnikov.doclib.service.exceptions.FileIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("rest/file")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class DocFileRestController {

    private final SecurityService securityService;
    private final DocFileService fileAct;

    public DocFileRestController(@Autowired SecurityService securityService, @Autowired DocFileService fileAct){
        this.fileAct=fileAct;
        this.securityService = securityService;
    }

    @PostMapping(value = "post", consumes = "multipart/data-form")
    public ResponseEntity<DocFileDto> uploadFile(@RequestPart("file") MultipartFile file) {
        String filePath = "doclib-web\\src\\main\\resources\\uploaded_files" + file.getOriginalFilename();
        try {
            Path savedFile = Paths.get(filePath);
            file.transferTo(savedFile.toAbsolutePath());
            DocFileDto docFileDto = new DocFileDto(0, file.getOriginalFilename(), savedFile.toAbsolutePath().toString());
            docFileDto = fileAct.createNewFile(docFileDto);
            return ResponseEntity.ok(docFileDto);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        } catch (FileIsAlreadyExistException e) {

            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> deleteFile(@PathVariable int id) {
        try {
            fileAct.deleteFile(id);
            return ResponseEntity.ok("File was successfully deleted");
        } catch (UnitNotFoundException e) {
            return ResponseEntity.badRequest().body("File wasn't created");
        }
    }

    @GetMapping(value = "get/{name}", produces = "multipart/form-data")
    @ResponseBody
    public ResponseEntity<FileSystemResource> getFile(@PathVariable String name) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try {
            DocFileDto docFileDto = fileAct.loadFile(name);
            if (securityService.checkPermission(docFileDto, auth.getName(), PermissionType.READING)) {
                File responseFile = new File(docFileDto.getPath());
                return ResponseEntity.ok(new FileSystemResource(responseFile));
            }
            return ResponseEntity.status(403).build();
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @GetMapping("get/{name}/permissions/get")
    public ResponseEntity<List<PermissionDto>> getPermissionList(@PathVariable String name){
        try {
            DocFileDto fileDto = fileAct.loadFile(name);
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (securityService.checkPermission(fileDto,auth.getName(),PermissionType.READING)) {
                return ResponseEntity.ok(securityService.getObjectPermissions(fileDto));
            }else{
                return ResponseEntity.status(403).build();
            }
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("get/{name}/permissions/post")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<PermissionDto>> postPermissionList(@RequestBody List<PermissionDto> permissionDtoList, @PathVariable String name){
        try {
            DocFileDto fileDto = fileAct.loadFile(name);
            securityService.updatePermissions(permissionDtoList);
            return ResponseEntity.ok(securityService.getObjectPermissions(fileDto));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

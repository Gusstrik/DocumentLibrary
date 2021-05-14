package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.service.DocFileActions;
import com.strelnikov.doclib.service.exceptions.FileIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import org.eclipse.jetty.server.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.RequestDispatcher;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
@Secured({"ROLE_USER", "ROLE_ADMIN"})
public class DocFileRestController {

    @Autowired
    private DocFileActions fileAct;

    @PostMapping("post")
    public ResponseEntity<DocFileDto> uploadFile(@RequestPart("file") MultipartFile file){
        String filePath = "doclib-web/src/main/resources/uploaded_files/"+file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath).getAbsoluteFile());
            DocFileDto docFileDto = new DocFileDto(0,file.getOriginalFilename(),filePath);
            docFileDto = fileAct.createNewFile(docFileDto);
            return ResponseEntity.ok(docFileDto);
        } catch (IOException | FileIsAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<String> deleteFile(@PathVariable int id){
        try{
            fileAct.deleteFile(id);
            return ResponseEntity.ok("File was successfully deleted");
        } catch (UnitNotFoundException e) {
            return ResponseEntity.badRequest().body("File wasn't created");
        }
    }

    @GetMapping(value = "get/{name}",produces = "multipart/form-data")
    @ResponseBody
    public FileSystemResource getFile(@PathVariable String name) throws UnitNotFoundException {
        DocFileDto docFileDto = fileAct.loadFile(name);
        File responseFile = new File(docFileDto.getPath());
        return new FileSystemResource(responseFile);
    }
}

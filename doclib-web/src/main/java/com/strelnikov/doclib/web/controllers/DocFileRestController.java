package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocFileDto;
import com.strelnikov.doclib.service.DocFileActions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class DocFileRestController {

    @Autowired
    private DocFileActions fileAct;

    @PostMapping("/upload")
    public ResponseEntity<DocFileDto> uploadFile(@RequestPart("file") MultipartFile file){
        String filePath = "doclib-web/src/main/resources/uploaded_files/"+file.getOriginalFilename();
        try {
            System.out.println(new File(filePath).getAbsolutePath());
            file.transferTo(new File(filePath).getAbsoluteFile());
            DocFileDto docFileDto = new DocFileDto(0,file.getOriginalFilename(),filePath);
            docFileDto = fileAct.createNewFile(docFileDto);
            return ResponseEntity.ok(docFileDto);
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }

    }
}

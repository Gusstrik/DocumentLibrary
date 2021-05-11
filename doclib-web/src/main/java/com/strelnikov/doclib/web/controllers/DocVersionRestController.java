package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.DocVersionDto;
import com.strelnikov.doclib.dto.DocumentDto;
import com.strelnikov.doclib.service.DocVersionActions;
import com.strelnikov.doclib.service.DocumentActions;
import com.strelnikov.doclib.service.exceptions.UnitNotFoundException;
import com.strelnikov.doclib.service.exceptions.VersionIsAlreadyExistException;
import com.strelnikov.doclib.service.exceptions.VersionNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/document/{id}/version")
public class DocVersionRestController {

    @Autowired
    DocVersionActions docVersionActions;

    @Autowired
    DocumentActions documentActions;

    @GetMapping
    public ResponseEntity<List<DocVersionDto>> getVersionList(@PathVariable("id") int id){
        try {
            return ResponseEntity.ok(docVersionActions.getVersionList(id));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("{version}")
    public ResponseEntity<Object> getDocVersion(@PathVariable("id") int id, @PathVariable("version") int version){
        try{
            return ResponseEntity.ok(documentActions.loadDocument(id,version));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).body("Document with id"+e.getUnitId()+" not found");
        } catch (VersionNotExistException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("{versionId}")
    public ResponseEntity<Object> deleteDocVersion(@PathVariable("id") int docId,@PathVariable("versionId") int versionId) throws UnitNotFoundException {
        docVersionActions.deleteDocVersion(versionId);
        return ResponseEntity.ok(docVersionActions.getVersionList(docId));
    }

    @DeleteMapping("/rollback/{version}")
    public ResponseEntity<Object> rollbackDoc(@PathVariable("id") int docId,@PathVariable("version") int version){
        try {
            documentActions.rollback(docId, version);
            return ResponseEntity.ok(docVersionActions.getVersionList(docId));
        } catch (UnitNotFoundException e) {
            return ResponseEntity.status(404).body("Document with id = " + e.getUnitId() + " doesn't exist");
        } catch (VersionNotExistException e) {
            return  ResponseEntity.status(404).body("There is no version " + e.getVersion() + " in document " + e.getDocId());
        }
    }

}

package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.service.ClientActions;
import com.strelnikov.doclib.service.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rest/user")
@Secured({"ROLE_ADMIN"})
public class ClientRestController {

    @Autowired
    private ClientActions clientActions;

    @GetMapping("/get/{login}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<ClientDto> getClientByLogin(@PathVariable String login) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.getAuthorities().contains("ROLE_ADMIN")) {
            if (!auth.getName().equals(login)) {
                return ResponseEntity.status(403).build();
            }
        }
        try {
            return ResponseEntity.ok(clientActions.loadClient(login));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<ClientDto> postClient(@RequestBody ClientDto clientDto){
        clientDto = clientActions.saveClient(clientDto);
        return ResponseEntity.ok(clientDto);
    }
}

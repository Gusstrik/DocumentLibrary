package com.strelnikov.doclib.web.controllers;

import com.strelnikov.doclib.dto.ClientDto;
import com.strelnikov.doclib.model.roles.AuthorityType;
import com.strelnikov.doclib.service.ClientService;
import com.strelnikov.doclib.service.exceptions.UserNotFoundException;
import com.strelnikov.doclib.web.security.AuthorityCheck;
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

    private final ClientService clientService;

    public  ClientRestController(@Autowired ClientService clientService){
        this.clientService = clientService;
    }

    @GetMapping("/get/{login}")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<ClientDto> getClientByLogin(@PathVariable String login) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!AuthorityCheck.hasAuthority(auth.getAuthorities(),AuthorityType.ROLE_ADMIN)) {
            if (!auth.getName().equals(login)) {
                return ResponseEntity.status(403).build();
            }
        }
        try {
            return ResponseEntity.ok(clientService.loadClient(login));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<ClientDto> postClient(@RequestBody ClientDto clientDto){
        clientDto = clientService.saveClient(clientDto);
        return ResponseEntity.ok(clientDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteClient(@PathVariable int id){
        try {
            clientService.deleteClient(id);
            return ResponseEntity.ok().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }
}

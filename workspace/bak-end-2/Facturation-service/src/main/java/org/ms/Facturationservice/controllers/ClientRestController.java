package org.ms.Facturationservice.controllers;


import org.ms.Facturationservice.dtos.ClientRequestDto;
import org.ms.Facturationservice.dtos.ClientResponseDto;
import org.ms.Facturationservice.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
//@RequestMapping("/client")
public class ClientRestController {
    @Autowired
    ClientService clientService;

    @GetMapping(path = "/clients")
    public List<ClientResponseDto> list() {
        return clientService.list();
    }


    @GetMapping(path = "/clients/{id}")
    public ClientResponseDto getOne(@PathVariable Long id) {
        return clientService.getOne(id);

    }


    @PostMapping(path = "/clients")
    public ResponseEntity<ClientResponseDto> save(@RequestBody ClientRequestDto clientRequestDto) {
        return clientService.save(clientRequestDto);

    }


    @PutMapping(path = "/clients/{id}")
    public ResponseEntity<ClientResponseDto> update(@PathVariable Long id, @Valid @RequestBody ClientRequestDto clientRequestDto) {
        return clientService.update(id, clientRequestDto);

    }


    @DeleteMapping(path = "/clients/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return clientService.delete(id);

    }
    @GetMapping(path = "/clients/bySociete/{id}")
    public List<ClientResponseDto> listBySocieteId(@PathVariable Long id){
        return clientService.listBySocieteId(id);

    }


}

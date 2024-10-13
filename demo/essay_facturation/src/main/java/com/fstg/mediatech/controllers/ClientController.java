package com.fstg.mediatech.controllers;

import com.fstg.mediatech.dto.ClientRequestDto;
import com.fstg.mediatech.dto.ClientResponseDto;
import com.fstg.mediatech.service.ClientService;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("clients")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("")
    //public List<ClientResponseDto> getClients() {
    public ResponseEntity<List<ClientResponseDto>> getClients() {
       // return clientService.findAll();
        return new ResponseEntity<>(clientService.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    //public ClientResponseDto save( @RequestBody() ClientRequestDto clientRequestDto) {
    public ResponseEntity<ClientResponseDto> save(@Valid @RequestBody() ClientRequestDto clientRequestDto) {
        //return  clientService.save(clientRequestDto);
        ClientResponseDto clientResponseDto = clientService.save(clientRequestDto);
        return new ResponseEntity<>(clientResponseDto, HttpStatus.CREATED);
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable("id") Integer id) {
        ClientResponseDto clientResponseDto = clientService.findById(id);
        return ResponseEntity.ok(clientResponseDto);
    }

    @GetMapping("/nom/{nom}")
    //public ClientResponseDto findByNom(@PathVariable("id") String nom) {
    public ResponseEntity<ClientResponseDto> findByNom(@PathVariable() String nom) {
        ClientResponseDto clientResponseDto = clientService.findByNom(nom);
        return ResponseEntity.ok(clientResponseDto);
    }

    @DeleteMapping("/id/{id}")
    //public void delete(@PathVariable() Integer id) {
    public ResponseEntity<?> delete(@PathVariable() Integer id) {
        clientService.delete(id); // hethi kenet khaw blech return
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/id/{id}")
    //public ClientResponseDto update(@RequestBody() ClientRequestDto clientRequestDto, @PathVariable() Integer id) throws NotFoundException {
    public ResponseEntity<ClientResponseDto> update(@Valid @RequestBody() ClientRequestDto clientRequestDto, @PathVariable() Integer id) throws NotFoundException {
       // return clientService.update(clientRequestDto, id);
        ClientResponseDto clientResponseDto = clientService.update(clientRequestDto, id);
        return ResponseEntity.accepted().body(clientResponseDto);
    }
}

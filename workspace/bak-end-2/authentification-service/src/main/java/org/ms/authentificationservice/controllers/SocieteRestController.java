package org.ms.authentificationservice.controllers;


import org.ms.authentificationservice.dtos.SocieteRequestDTO;
import org.ms.authentificationservice.dtos.SocieteResponseDTO;
import org.ms.authentificationservice.services.SocieteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class SocieteRestController {
    private final SocieteService societeService;


    public SocieteRestController(SocieteService societeService) {
        this.societeService = societeService;

    }

    @GetMapping(path = "/societes")
    //@PreAuthorize("hasAuthority('SUPER ADMIN')")
    public List<SocieteResponseDTO> list() {
        return societeService.list();
    }


    @GetMapping(path = "/societes/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public SocieteResponseDTO getOne(@PathVariable Long id) {
        return societeService.getOne(id);

    }


    @PostMapping(path = "/societes")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SocieteResponseDTO> save(@RequestBody SocieteRequestDTO societeRequestDTO) {
        return societeService.save(societeRequestDTO);

    }


    @PutMapping(path = "/societes/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SocieteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SocieteRequestDTO societeRequestDTO) {
        return societeService.update(id, societeRequestDTO);

    }


    @DeleteMapping(path = "/societes/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return societeService.delete(id);

    }
}

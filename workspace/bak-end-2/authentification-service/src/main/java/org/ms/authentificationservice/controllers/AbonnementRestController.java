package org.ms.authentificationservice.controllers;

import org.ms.authentificationservice.dtos.AbonnementRequestDTO;
import org.ms.authentificationservice.dtos.AbonnementResponseDTO;
import org.ms.authentificationservice.services.AbonnementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
public class AbonnementRestController {

    private final AbonnementService abonnementService;


    public AbonnementRestController(AbonnementService abonnementService) {
        this.abonnementService = abonnementService;

    }
    @GetMapping(path = "/abonnements")
    // @PreAuthorize("hasAuthority('SUPER ADMIN')")
    public List<AbonnementResponseDTO> list() {
        return abonnementService.list();
    }


    @GetMapping(path = "/abonnements/{id}")
    //@PreAuthorize("hasAuthority('SUPER ADMIN')")
    public AbonnementResponseDTO getOne(@PathVariable Long id) {
        return abonnementService.getOne(id);

    }
    @GetMapping(path = "/abonnements/bySociete/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public AbonnementResponseDTO getAbonnementBySociete(@PathVariable Long id){
        return abonnementService.getAbonnementBySociete(id);
    }


    @PostMapping(path = "/abonnements")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AbonnementResponseDTO> save(@RequestBody AbonnementRequestDTO abonnementRequestDTO) {
        return abonnementService.save(abonnementRequestDTO);

    }


    @PutMapping(path = "/abonnements/{id}")
    //@PostAuthorize("hasAuthority('SUPER ADMIN')")
    public ResponseEntity<AbonnementResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AbonnementRequestDTO abonnementRequestDTO) {
        return abonnementService.update(id, abonnementRequestDTO);

    }


    @DeleteMapping(path = "/abonnements/{id}")
    //@PostAuthorize("hasAuthority('SUPER ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return abonnementService.delete(id);

    }
    @PostMapping(path = "/abonnements/renouvellement/{id}")
    //@PostAuthorize("hasAuthority('SUPER ADMIN')")
    public ResponseEntity<AbonnementResponseDTO> renouvelerAbonnement(@PathVariable Long id){

        return  abonnementService.renouvelerAbonnement(id);

    }
    @PostMapping(path = "/abonnements/suspendre/{id}")
    //@PostAuthorize("hasAuthority('SUPER ADMIN')")
    public ResponseEntity<AbonnementResponseDTO> suspendreAbonnement(@PathVariable Long id){
        return abonnementService.suspendreAbonnement(id);
    }
    @PostMapping(path = "/abonnements/activation/{id}")
    //@PostAuthorize("hasAuthority('SUPER ADMIN')")
    public ResponseEntity<AbonnementResponseDTO> activerAbonnement(@PathVariable Long id){
        return abonnementService.activerAbonnement(id);
    }


}

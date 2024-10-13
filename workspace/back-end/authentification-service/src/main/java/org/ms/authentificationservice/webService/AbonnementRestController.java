package org.ms.authentificationservice.webService;

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
    public List<AbonnementResponseDTO> list() {
        return abonnementService.list();
    }


    @GetMapping(path = "/abonnements/{id}")
    public AbonnementResponseDTO getOne(@PathVariable Long id) {
        return abonnementService.getOne(id);

    }


    @PostMapping(path = "/abonnements")
    public ResponseEntity<AbonnementResponseDTO> save(@RequestBody AbonnementRequestDTO abonnementRequestDTO) {
        return abonnementService.save(abonnementRequestDTO);

    }


    @PutMapping(path = "/abonnements/{id}")
    public ResponseEntity<AbonnementResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AbonnementRequestDTO abonnementRequestDTO) {
        return abonnementService.update(id, abonnementRequestDTO);

    }


    @DeleteMapping(path = "/abonnements/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return abonnementService.delete(id);

    }
    @PostMapping(path = "/abonnements/renouvellement/{id}")
    public ResponseEntity<AbonnementResponseDTO> renouvelerAbonnement(@PathVariable Long id){

        return  abonnementService.renouvelerAbonnement(id);

    }
    @PostMapping(path = "/abonnements/suspendre/{id}")
    public ResponseEntity<AbonnementResponseDTO> suspendreAbonnement(@PathVariable Long id){
        return abonnementService.suspendreAbonnement(id);
    }
    @PostMapping(path = "/abonnements/activation/{id}")
    public ResponseEntity<AbonnementResponseDTO> activerAbonnement(@PathVariable Long id){
        return abonnementService.activerAbonnement(id);
    }


}

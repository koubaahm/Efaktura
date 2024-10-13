package org.ms.Facturationservice.webService;




import org.ms.Facturationservice.dto.FactureRequestDto;
import org.ms.Facturationservice.dto.FactureResponseDto;
import org.ms.Facturationservice.service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
public class FactureRestController {

    @Autowired
    FactureService factureService;

    @GetMapping(path = "/factures")
    public List<FactureResponseDto> list() {
        return factureService.list();

    }


    @GetMapping(path = "/factures/paye")
    public List<FactureResponseDto> listFacturesPaye() {
        return factureService.listFacturesPaye();

    }


    @GetMapping(path = "/factures/{id}")
    public FactureResponseDto getOne(@PathVariable Long id) {
        return factureService.getOne(id);

    }


    @PostMapping(path = "/factures")
    public ResponseEntity<FactureResponseDto> save(@RequestBody @Valid FactureRequestDto factureRequestDto) {
        return factureService.save(factureRequestDto);


    }


    @PutMapping(path = "/factures/{id}")
    public FactureResponseDto update(@PathVariable Long id, @RequestBody @Valid FactureRequestDto factureRequestDto) {
        return factureService.update(id, factureRequestDto);

    }


    @DeleteMapping("/factures/{id}")
    public ResponseEntity<?> deleteFacture(@PathVariable Long id) {
        return factureService.delete(id);

    }
    @GetMapping("factures/numeroNouvelleFacture")
    public String getNumeroFacture(Long idSoc) {

       return factureService.generateNumeroFacture(idSoc);

    }
    @GetMapping("factures/lastNumeroFacture")
    private String gelastNumeroFactureBySociete(Long idSoc){

        return factureService.getlastNumeroFactureBySociete(idSoc);

    }
    @GetMapping(path = "/factures/bySociete/{id}")
    public List<FactureResponseDto> listBySocieteId(@PathVariable Long id){
        return factureService.listBySocieteId(id);

    }

}
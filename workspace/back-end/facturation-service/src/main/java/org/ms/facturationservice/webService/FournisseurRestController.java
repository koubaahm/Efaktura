package org.ms.Facturationservice.webService;


import org.ms.Facturationservice.dto.FournisseurRequestDto;
import org.ms.Facturationservice.dto.FournisseurResponseDto;
import org.ms.Facturationservice.service.FournisseurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/fournisseurs")
public class FournisseurRestController {



    @Autowired
    FournisseurService fournisseurService;



    @GetMapping("")
    public ResponseEntity<List<FournisseurResponseDto>> list() {
        List<FournisseurResponseDto> fournisseurs = fournisseurService.list();
        return new ResponseEntity<>(fournisseurs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurResponseDto> getOne(@PathVariable Long id) {
        FournisseurResponseDto fournisseur = fournisseurService.getOne(id);
        return new ResponseEntity<>(fournisseur, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<FournisseurResponseDto> save(@Valid @RequestBody FournisseurRequestDto fournisseurRequestDto) {
        FournisseurResponseDto createdFournisseur = fournisseurService.save(fournisseurRequestDto);
        return new ResponseEntity<>(createdFournisseur, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FournisseurResponseDto> update(@PathVariable Long id, @Valid @RequestBody FournisseurRequestDto fournisseurRequestDto) {
        FournisseurResponseDto updatedFournisseur = fournisseurService.update(id, fournisseurRequestDto);
        return new ResponseEntity<>(updatedFournisseur, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        fournisseurService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/bySociete/{id}")
    public List<FournisseurResponseDto> listBySocieteId(@PathVariable  Long id){
        return fournisseurService.listBySocieteId(id);

    }

}


package org.ms.Facturationservice.controllers;


import org.ms.Facturationservice.dtos.LigneAchatRequestDto;
import org.ms.Facturationservice.dtos.LigneAchatResponseDto;
import org.ms.Facturationservice.services.LigneAchatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
public class LigneAchatRestController {
    @Autowired
    LigneAchatService ligneAchatService;

    @GetMapping(path = "/ligneAchats")
    public List<LigneAchatResponseDto> list() {
        return ligneAchatService.list();

    }

    @GetMapping(path = "/ligneAchats/{id}")
    public LigneAchatResponseDto getOne(@PathVariable Long id) {
        return ligneAchatService.getOne(id);

    }

    @PostMapping(path = "/ligneAchats")
    public LigneAchatResponseDto save(@RequestBody LigneAchatRequestDto ligneAchatRequestDto) {
        return ligneAchatService.save(ligneAchatRequestDto);

    }
    @PostMapping(path = "/ligneAchats/saveList")
    public List<LigneAchatResponseDto> saveList(@RequestBody ArrayList<LigneAchatRequestDto> ligneAchatRequestDtoList){
        return ligneAchatService.saveList(ligneAchatRequestDtoList);
    }


    @PutMapping(path = "/ligneAchats/{id}")
    public LigneAchatResponseDto update(@PathVariable Long id, @RequestBody LigneAchatRequestDto ligneAchatRequestDto) {
        return ligneAchatService.update(id, ligneAchatRequestDto);

    }
    @PutMapping(path = "/ligneAchats/updateList")
    public List<LigneAchatResponseDto> updateList(@RequestBody List<LigneAchatRequestDto> ligneAchatRequestDtoList){

        return ligneAchatService.updateList(ligneAchatRequestDtoList);
    }


    @DeleteMapping(path = "/ligneAchats/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ligneAchatService.deleteLigneAchat(id);

    }
    @GetMapping(path = "/ligneAchats/getAllByProduit/{id}")
    public List<LigneAchatResponseDto> getAllByProduit(@PathVariable Long id){
        return ligneAchatService.getAllByProduit(id);
    }
    @GetMapping(path = "/ligneAchats/bySociete/{id}")
    public List<LigneAchatResponseDto> listBySocieteId(@PathVariable Long id){
        return  ligneAchatService.listBySocieteId(id);

    }
    @GetMapping(path = "/ligneAchats/byProduit/{id}")
    public List<LigneAchatResponseDto> listByProduitId(@PathVariable Long id){
        return ligneAchatService.listByProduitId(id);


    }
}

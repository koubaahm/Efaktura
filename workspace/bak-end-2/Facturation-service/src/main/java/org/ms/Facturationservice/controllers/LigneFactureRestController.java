package org.ms.Facturationservice.controllers;



import org.ms.Facturationservice.dtos.LigneFactureRequestDto;
import org.ms.Facturationservice.dtos.LigneFactureResponseDto;
import org.ms.Facturationservice.services.LigneFactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
//@RequestMapping("/api/factureLignes")
public class LigneFactureRestController {

    @Autowired
    LigneFactureService ligneFactureService;

    @GetMapping(path = "/ligneFactures")
    public List<LigneFactureResponseDto> list() {
        return ligneFactureService.list();

    }

    @GetMapping(path = "/ligneFactures/{id}")
    public LigneFactureResponseDto getOne(@PathVariable Long id) {
        return ligneFactureService.getOne(id);

    }

    @PostMapping(path = "/ligneFactures")
    public LigneFactureResponseDto save(@RequestBody LigneFactureRequestDto ligneFactureRequestDto) {
        return ligneFactureService.save(ligneFactureRequestDto);

    }


    @PostMapping(path = "/ligneFactures/saveList")
    public List<LigneFactureResponseDto> saveList(@RequestBody ArrayList<LigneFactureRequestDto> ligneFactureRequestDtos) {
        return ligneFactureService.saveList(ligneFactureRequestDtos);
    }



    @PutMapping(path = "/ligneFactures/{id}")
    public LigneFactureResponseDto update(@PathVariable Long id, @RequestBody LigneFactureRequestDto ligneFactureRequestDto) {
        return ligneFactureService.update(id, ligneFactureRequestDto);

    }
   @PutMapping(path="/ligneFactures/updateList")
    public List<LigneFactureResponseDto> updateList(@RequestBody List<LigneFactureRequestDto> ligneFactureRequestDtos){
        return  ligneFactureService.updateList(ligneFactureRequestDtos);
   }


    @DeleteMapping(path = "/ligneFactures/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return ligneFactureService.deleteLigneFacture(id);

    }
    @GetMapping(path = "/ligneFactures/byIdFacture/{id}")
    public List<LigneFactureResponseDto> listByFactureId(@PathVariable Long id){
        return ligneFactureService.listByFactureId(id);

    }


}

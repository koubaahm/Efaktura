package org.ms.Facturationservice.controllers;


import org.ms.Facturationservice.dtos.TvaRequestDto;
import org.ms.Facturationservice.dtos.TvaResponseDto;
import org.ms.Facturationservice.services.TvaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
//@RequestMapping("/client")
public class TvaRestController {
    @Autowired
    TvaService tvaService;

    @GetMapping(path = "/tvas")
    public List<TvaResponseDto> list() {
        return tvaService.list();

    }


    @GetMapping(path = "/tvas/{id}")
    public TvaResponseDto getOne(@PathVariable Long id) {
        return tvaService.getOne(id);

    }


    @PostMapping(path = "/tvas")
    public ResponseEntity<TvaResponseDto> save(@RequestBody TvaRequestDto tvaRequestDto) {
        return tvaService.save(tvaRequestDto);

    }


    @PutMapping(path = "/tvas/{id}")
    public ResponseEntity<TvaResponseDto> update(@PathVariable Long id, @Valid @RequestBody TvaRequestDto tvaRequestDto) {
        return tvaService.update(id, tvaRequestDto);
    }


    @DeleteMapping(path = "/tvas/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return tvaService.delete(id);

    }
    @GetMapping(path = "/tvas/bySociete/{id}")
    public List<TvaResponseDto> listBySocieteId(@PathVariable Long id){
        return  tvaService.listBySocieteId(id);

    }


}

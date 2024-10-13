package org.ms.Facturationservice.feign;


import org.ms.Facturationservice.entities.SocieteRequestDTO;
import org.ms.Facturationservice.entities.SocieteResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@FeignClient(name="AUTHENTIFICATION-SERVICE")
public interface SocieteServiceFeign {
    @GetMapping(path = "/societes")
    public List<SocieteResponseDTO> list();


    @GetMapping(path = "/societes/{id}")
    public SocieteResponseDTO getOne(@PathVariable Long id);



    @PostMapping(path = "/societes")
    public ResponseEntity<SocieteResponseDTO> save(@RequestBody SocieteRequestDTO societeRequestDTO) ;





    @PutMapping(path = "/societes/{id}")
    public ResponseEntity<SocieteResponseDTO> update(@PathVariable Long id, @Valid @RequestBody SocieteRequestDTO societeRequestDTO);



    @DeleteMapping(path = "/societes/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) ;



}

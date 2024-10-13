package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.ProduitRequestDto;
import org.ms.Facturationservice.dtos.ProduitResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface ProduitService {
    List<ProduitResponseDto> list();
    ResponseEntity<ProduitResponseDto> getOne( Long id);
    ResponseEntity<ProduitResponseDto> save(ProduitRequestDto produitRequestDto);

    ResponseEntity<ProduitResponseDto> update( Long id,ProduitRequestDto produitRequestDto);

    ResponseEntity<?> delete(Long id);

    List<ProduitResponseDto> listBySocieteId(Long societeId);
}

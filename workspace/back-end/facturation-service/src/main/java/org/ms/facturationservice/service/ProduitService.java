package org.ms.Facturationservice.service;


import org.ms.Facturationservice.dto.ProduitRequestDto;
import org.ms.Facturationservice.dto.ProduitResponseDto;
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

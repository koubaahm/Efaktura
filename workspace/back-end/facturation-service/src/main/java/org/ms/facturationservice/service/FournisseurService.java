package org.ms.Facturationservice.service;


import org.ms.Facturationservice.dto.FournisseurRequestDto;
import org.ms.Facturationservice.dto.FournisseurResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FournisseurService {
    List<FournisseurResponseDto> list();

    List<FournisseurResponseDto> listBySocieteId(Long societeId);

    FournisseurResponseDto getOne(Long id);


    FournisseurResponseDto save(FournisseurRequestDto fournisseurRequestDto);


    FournisseurResponseDto update(Long id, FournisseurRequestDto fournisseurRequestDto);

    ResponseEntity<?> delete(Long id);
}


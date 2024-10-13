package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.FournisseurRequestDto;
import org.ms.Facturationservice.dtos.FournisseurResponseDto;
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


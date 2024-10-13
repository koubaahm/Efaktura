package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.LigneFactureRequestDto;
import org.ms.Facturationservice.dtos.LigneFactureResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


public interface LigneFactureService {
    List<LigneFactureResponseDto> list();

    LigneFactureResponseDto getOne(Long id);

    LigneFactureResponseDto save(LigneFactureRequestDto ligneFactureRequestDto);

    List<LigneFactureResponseDto> updateList(List<LigneFactureRequestDto> ligneFactureRequestDtos);

    List<LigneFactureResponseDto> saveList(ArrayList<LigneFactureRequestDto> ligneFactureRequestDtos);

    LigneFactureResponseDto update(Long id, LigneFactureRequestDto ligneFactureRequestDto);

    ResponseEntity<?> deleteLigneFacture(Long id);

    List<LigneFactureResponseDto> listByFactureId(Long factureId);

}

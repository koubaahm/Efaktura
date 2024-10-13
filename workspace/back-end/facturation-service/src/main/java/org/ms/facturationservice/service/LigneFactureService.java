package org.ms.Facturationservice.service;



import org.ms.Facturationservice.dto.LigneFactureRequestDto;
import org.ms.Facturationservice.dto.LigneFactureResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


public interface LigneFactureService {
    List<LigneFactureResponseDto> list();

     LigneFactureResponseDto getOne( Long id);
    LigneFactureResponseDto save( LigneFactureRequestDto ligneFactureRequestDto);

    public List<LigneFactureResponseDto> updateList(List<LigneFactureRequestDto> ligneFactureRequestDtos);

    public List<LigneFactureResponseDto> saveList(ArrayList<LigneFactureRequestDto> ligneFactureRequestDtos);

    LigneFactureResponseDto update( Long id,  LigneFactureRequestDto ligneFactureRequestDto);

     ResponseEntity<?> deleteLigneFacture(Long id);

    public List<LigneFactureResponseDto> listByFactureId(Long factureId);

}

package org.ms.Facturationservice.services;



import org.ms.Facturationservice.dtos.LigneAchatRequestDto;
import org.ms.Facturationservice.dtos.LigneAchatResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;


public interface LigneAchatService {

        List<LigneAchatResponseDto> list();

        List<LigneAchatResponseDto> listBySocieteId(Long societeId);
         List<LigneAchatResponseDto> listByProduitId(Long produitId);
        LigneAchatResponseDto getOne( Long id);
        LigneAchatResponseDto save( LigneAchatRequestDto ligneAchatRequestDto);
        List<LigneAchatResponseDto> saveList(ArrayList<LigneAchatRequestDto> ligneAchatRequestDtoList);
        LigneAchatResponseDto update( Long id,  LigneAchatRequestDto ligneAchatRequestDto);
         List<LigneAchatResponseDto> updateList(List<LigneAchatRequestDto> ligneAchatRequestDtoList);

        ResponseEntity<?> deleteLigneAchat(Long id);

        List<LigneAchatResponseDto> getAllByProduit(Long produitId);
    }



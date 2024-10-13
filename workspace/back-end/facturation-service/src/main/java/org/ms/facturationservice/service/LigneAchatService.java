package org.ms.Facturationservice.service;



import org.ms.Facturationservice.dto.LigneAchatRequestDto;
import org.ms.Facturationservice.dto.LigneAchatResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface LigneAchatService {

        List<LigneAchatResponseDto> list();

        List<LigneAchatResponseDto> listBySocieteId(Long societeId);

        LigneAchatResponseDto getOne( Long id);
        LigneAchatResponseDto save( LigneAchatRequestDto ligneAchatRequestDto);

        LigneAchatResponseDto update( Long id,  LigneAchatRequestDto ligneAchatRequestDto);

        ResponseEntity<?> deleteLigneAchat(Long id);

        List<LigneAchatResponseDto> getAllByProduit(Long produitId);
    }



package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.FactureRequestDto;
import org.ms.Facturationservice.dtos.FactureResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface FactureService {


    List<FactureResponseDto> list();
    List<FactureResponseDto> listFacturesByStatut(String statut);

    FactureResponseDto getOne(Long id);

    ResponseEntity<FactureResponseDto> save(FactureRequestDto factureRequestDto);


    FactureResponseDto update( Long id, FactureRequestDto factureRequestDto);

    ResponseEntity<?> delete( Long id);
    String generateNumeroFacture(Long idSoc);

    String getlastNumeroFactureBySociete(Long idSoc);


    List<FactureResponseDto> listBySocieteId(Long societeId);

}

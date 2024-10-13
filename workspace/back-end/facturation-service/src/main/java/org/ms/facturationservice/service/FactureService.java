package org.ms.Facturationservice.service;


import org.ms.Facturationservice.dto.FactureRequestDto;
import org.ms.Facturationservice.dto.FactureResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public interface FactureService {


    public List<FactureResponseDto> list();
    public List<FactureResponseDto> listFacturesPaye();

    public FactureResponseDto getOne(Long id);

    public ResponseEntity<FactureResponseDto> save(FactureRequestDto factureRequestDto);


    FactureResponseDto update( Long id, FactureRequestDto factureRequestDto);

    ResponseEntity<?> delete( Long id);
    String generateNumeroFacture(Long idSoc);

    String getlastNumeroFactureBySociete(Long idSoc);


    List<FactureResponseDto> listBySocieteId(Long societeId);

}

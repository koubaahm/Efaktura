package org.ms.Facturationservice.services;



import org.ms.Facturationservice.dtos.TvaRequestDto;
import org.ms.Facturationservice.dtos.TvaResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TvaService {
    List<TvaResponseDto> list();
    TvaResponseDto getOne(Long id) ;
    ResponseEntity<TvaResponseDto> save(TvaRequestDto stockRequestDto);
    ResponseEntity<TvaResponseDto> update( Long id,TvaRequestDto stockRequestDto);
    ResponseEntity<?> delete(Long id);
    List <TvaResponseDto> listBySocieteId(Long societeId);
}

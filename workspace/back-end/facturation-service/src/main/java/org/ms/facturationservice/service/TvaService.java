package org.ms.Facturationservice.service;



import org.ms.Facturationservice.dto.TvaRequestDto;
import org.ms.Facturationservice.dto.TvaResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface TvaService {
    List<TvaResponseDto> list();
    TvaResponseDto getOne(Long id) ;
    ResponseEntity<TvaResponseDto> save(TvaRequestDto stockRequestDto);
    ResponseEntity<TvaResponseDto> update( Long id,TvaRequestDto stockRequestDto);
    ResponseEntity<?> delete(Long id);
    public List <TvaResponseDto> listBySocieteId(Long societeId);
}

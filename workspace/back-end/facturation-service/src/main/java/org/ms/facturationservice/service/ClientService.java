package org.ms.Facturationservice.service;


import org.ms.Facturationservice.dto.ClientRequestDto;
import org.ms.Facturationservice.dto.ClientResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface ClientService {
    public List<ClientResponseDto> list();

    ClientResponseDto getOne(Long id);

    ResponseEntity<ClientResponseDto> save(ClientRequestDto clientRequestDto);

    ResponseEntity<ClientResponseDto> update(@PathVariable Long id, ClientRequestDto clientRequestDto);

    ResponseEntity<?>  delete(Long id);

    List<ClientResponseDto> listBySocieteId(Long societeId);


}

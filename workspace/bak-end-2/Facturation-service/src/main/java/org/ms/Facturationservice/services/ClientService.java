package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.ClientRequestDto;
import org.ms.Facturationservice.dtos.ClientResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


public interface ClientService {
    List<ClientResponseDto> list();

    ClientResponseDto getOne(Long id);

    ResponseEntity<ClientResponseDto> save(ClientRequestDto clientRequestDto);

    ResponseEntity<ClientResponseDto> update(@PathVariable Long id, ClientRequestDto clientRequestDto);

    ResponseEntity<?>  delete(Long id);

    List<ClientResponseDto> listBySocieteId(Long societeId);


}

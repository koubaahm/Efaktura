package org.ms.authentificationservice.services;

import org.ms.authentificationservice.dtos.SocieteRequestDTO;
import org.ms.authentificationservice.dtos.SocieteResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SocieteService {
    public List<SocieteResponseDTO> list();

    public SocieteResponseDTO getOne(Long id);

    public ResponseEntity<SocieteResponseDTO> save(SocieteRequestDTO societeResponseDTO);

    public ResponseEntity<SocieteResponseDTO> update(@PathVariable Long id, SocieteRequestDTO societeResponseDTO);

    public ResponseEntity<?> delete(Long id);
}

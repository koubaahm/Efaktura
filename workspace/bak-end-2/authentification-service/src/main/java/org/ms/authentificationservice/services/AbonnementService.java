package org.ms.authentificationservice.services;


import org.ms.authentificationservice.dtos.AbonnementRequestDTO;
import org.ms.authentificationservice.dtos.AbonnementResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AbonnementService {
    public List<AbonnementResponseDTO> list();

    public AbonnementResponseDTO getOne(Long id);

    AbonnementResponseDTO getAbonnementBySociete(Long idSociete);

    public ResponseEntity<AbonnementResponseDTO> save(AbonnementRequestDTO societeResponseDTO);

    public ResponseEntity<AbonnementResponseDTO> update(@PathVariable Long id, AbonnementRequestDTO societeResponseDTO);

    public ResponseEntity<?> delete(Long id);

    public ResponseEntity<AbonnementResponseDTO> renouvelerAbonnement(Long id);

    public ResponseEntity<AbonnementResponseDTO> suspendreAbonnement(Long id);

    public ResponseEntity<AbonnementResponseDTO> activerAbonnement(Long id);
}

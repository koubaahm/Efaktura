package org.ms.authentificationservice.services;

import org.ms.authentificationservice.dtos.AbonnementRequestDTO;
import org.ms.authentificationservice.dtos.AbonnementResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.Societe;
import org.ms.authentificationservice.mapping.AbonnementMapper;
import org.ms.authentificationservice.repositories.AbonnementRepository;
import org.ms.authentificationservice.repositories.SocieteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class AbonnementServiceImpl implements AbonnementService {

    private final AbonnementRepository abonnementRepository;
    private final AbonnementMapper abonnementMapper;

    private final SocieteRepository societeRepository;

    public AbonnementServiceImpl(AbonnementRepository abonnementRepository, AbonnementMapper abonnementMapper, SocieteRepository societeRepository) {
        this.abonnementRepository = abonnementRepository;
        this.abonnementMapper = abonnementMapper;
        this.societeRepository = societeRepository;
    }

    @Override
    public List<AbonnementResponseDTO> list() {
        List<Abonnement> abonnements = abonnementRepository.findAll();
        List<AbonnementResponseDTO> abonnementResponseDTOS = new ArrayList<>();

        for (Abonnement abonnement : abonnements) {
            AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
            if (abonnement.getSociete() != null) {
                abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());

            }
            abonnementResponseDTOS.add(abonnementResponseDTO);
        }
        return abonnementResponseDTOS;
    }

    @Override
    public AbonnementResponseDTO getOne(Long id) {
        Optional<Abonnement> optionalAbonnement = abonnementRepository.findById(id);
        Abonnement abonnement = optionalAbonnement.orElseThrow(() -> new EntityNotFoundException("Abonnement not found"));
        AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);

        if (abonnement.getSociete() != null) {
            abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());

        }

        return abonnementResponseDTO;
    }

//    @Override
//    public ResponseEntity<AbonnementResponseDTO> save(AbonnementRequestDTO abonnementRequestDTO) {
//        Abonnement abonnement = abonnementMapper.modelMapper().map(abonnementRequestDTO, Abonnement.class);
//        Societe societe = societeRepository.findById(abonnementRequestDTO.getSocieteId()).orElse(null);
//        abonnement.setSociete(societe);
//
//        abonnementRepository.save(abonnement);
//
//        AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
//        abonnementResponseDTO.setSocieteId(societe.getId());
//
//        return new ResponseEntity<>(abonnementResponseDTO, HttpStatus.CREATED);
//    }
@Override
public ResponseEntity<AbonnementResponseDTO> save(AbonnementRequestDTO abonnementRequestDTO) {
    Abonnement abonnement = abonnementMapper.modelMapper().map(abonnementRequestDTO, Abonnement.class);
    abonnement.setActive(true);

    abonnementRepository.save(abonnement);

    AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);

    if (abonnement.getSociete() != null) {
        abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());
    }

    return new ResponseEntity<>(abonnementResponseDTO, HttpStatus.CREATED);
}








    @Override
    public ResponseEntity<AbonnementResponseDTO> update(Long id, AbonnementRequestDTO abonnementRequestDTO) {
        Optional<Abonnement> abonnementToUpdate = abonnementRepository.findById(id);


        if (abonnementToUpdate.isPresent()) {

            Abonnement abonnement = abonnementToUpdate.get();

            abonnement.setActive(abonnementRequestDTO.getActive());


            abonnementRepository.save(abonnement);
            AbonnementResponseDTO updatedAbonnementResponseDto = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
            return new ResponseEntity<>(updatedAbonnementResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Abonnement abonnement = abonnementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Abonnement not found with id " + id));
        if (abonnement != null) {
            abonnementRepository.delete(abonnement);
            return new ResponseEntity<>("Abonnement supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Abonnement non trouvée.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<AbonnementResponseDTO> renouvelerAbonnement(Long id) {
        Optional<Abonnement> optionalAbonnement = abonnementRepository.findById(id);
        Abonnement abonnement = optionalAbonnement.orElseThrow(() -> new EntityNotFoundException("Abonnement not found"));

        // Mettez ici la logique de renouvellement de l'abonnement
//        LocalDate nouvelleDateFin = abonnement.getDateFin().plusYears(1);
        LocalDateTime nouvelleDateFin = abonnement.getDateFin().plusYears(1);
        abonnement.setDateFin(nouvelleDateFin);
        abonnement.setActive(true);

        abonnementRepository.save(abonnement);

        AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
        abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());

        return new ResponseEntity<>(abonnementResponseDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AbonnementResponseDTO> suspendreAbonnement(Long id) {
        Optional<Abonnement> optionalAbonnement = abonnementRepository.findById(id);
        Abonnement abonnement = optionalAbonnement.orElseThrow(() -> new EntityNotFoundException("Abonnement not found"));

        // Mettez ici la logique de suspension de l'abonnement
        abonnement.setActive(false);

        abonnementRepository.save(abonnement);

        AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
        abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());

        return new ResponseEntity<>(abonnementResponseDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AbonnementResponseDTO> activerAbonnement(Long id) {
        Optional<Abonnement> optionalAbonnement = abonnementRepository.findById(id);
        Abonnement abonnement = optionalAbonnement.orElseThrow(() -> new EntityNotFoundException("Abonnement not found"));

        // Mettez ici la logique d'activation de l'abonnement
        abonnement.setActive(true);
        LocalDateTime nouvelleDateFin = abonnement.getDateFin().plusDays(2);
        abonnement.setDateFin(nouvelleDateFin);

        abonnementRepository.save(abonnement);

        AbonnementResponseDTO abonnementResponseDTO = abonnementMapper.modelMapper().map(abonnement, AbonnementResponseDTO.class);
        abonnementResponseDTO.setSocieteId(abonnement.getSociete().getId());

        return new ResponseEntity<>(abonnementResponseDTO, HttpStatus.OK);
    }


}

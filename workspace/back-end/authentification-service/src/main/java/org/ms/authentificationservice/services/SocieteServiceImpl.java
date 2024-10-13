package org.ms.authentificationservice.services;


import org.ms.authentificationservice.dtos.SocieteRequestDTO;
import org.ms.authentificationservice.dtos.SocieteResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.ms.authentificationservice.mapping.SocieteMapper;
import org.ms.authentificationservice.repositories.AbonnementRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.ms.authentificationservice.repositories.SocieteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SocieteServiceImpl implements SocieteService {

    private final SocieteRepository societeRepository;

    private final SocieteMapper societeMapper;

    private final AppUserRepository appUserRepository;

    private final AbonnementRepository abonnementRepository;


    public SocieteServiceImpl(SocieteRepository societeRepository, SocieteMapper societeMapper, AppUserRepository appUserRepository,AbonnementRepository abonnementRepository) {
        this.societeRepository = societeRepository;
        this.societeMapper = societeMapper;
        this.appUserRepository = appUserRepository;
        this.abonnementRepository=abonnementRepository;
    }

    @Override
    public List<SocieteResponseDTO> list() {
        List<Societe> societes = societeRepository.findAll();
        List<SocieteResponseDTO> societeResponseDTOS = new ArrayList<>();

        for (Societe societe : societes) {
            SocieteResponseDTO societeResponseDTO = societeMapper.modelMapper().map(societe, SocieteResponseDTO.class);
            societeResponseDTO.setUtilisateurIds(societe.getOperateurs().stream().map(AppUser::getId).collect(Collectors.toList()));
            societeResponseDTO.setAbonnementId(societe.getAbonnement().getId());
            societeResponseDTO.setAdminUserId(societe.getAdminUser().getId());
            societeResponseDTOS.add(societeResponseDTO);
        }
        return societeResponseDTOS;
    }

    @Override
    public SocieteResponseDTO getOne(Long id) {
        Optional<Societe> optionalSociete = societeRepository.findById(id);
        Societe societe = optionalSociete.orElseThrow(() -> new EntityNotFoundException("Societe not found"));

        List<Long> utilisateurIds = societe.getOperateurs().stream().map(AppUser::getId).collect(Collectors.toList());

        SocieteResponseDTO societeResponseDTO = societeMapper.modelMapper().map(societe, SocieteResponseDTO.class);
        societeResponseDTO.setUtilisateurIds(utilisateurIds);
        if (societe.getAbonnement() != null) {
            societeResponseDTO.setAbonnementId(societe.getAbonnement().getId());
        }

        societeResponseDTO.setAdminUserId(societe.getAdminUser().getId());


        return societeResponseDTO;
    }


    @Override
    public ResponseEntity<SocieteResponseDTO> save(SocieteRequestDTO societeRequestDTO) {

        // Vérifier si l'abonnement est déjà associé à une autre société
        Abonnement abonnement = abonnementRepository.findById(societeRequestDTO.getAbonnementId()).orElse(null);
        Societe existingSociete = societeRepository.findByAbonnement(abonnement);
        if (existingSociete != null) {
            throw new IllegalStateException("Cet abonnement est déjà associé à une autre société");
        }

        // Créer l'objet Societe à partir de SocieteRequestDTO
        Societe societe = societeMapper.modelMapper().map(societeRequestDTO, Societe.class);

        // Vérifier si l'abonnement est fourni dans la requête
        if (societeRequestDTO.getAbonnementId() != null) {
            // Associer la Societe à l'Abonnement
            assert abonnement != null;
            abonnement.setSociete(societe);
            societe.setAbonnement(abonnement);
        }

        // Vérifier si l'identifiant de l'utilisateur admin est fourni dans la requête
        if (societeRequestDTO.getAdminUserId() == null) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur admin doit être fourni");
        }

        // Récupérer l'utilisateur admin à partir de son identifiant
        AppUser adminUser = appUserRepository.findById(societeRequestDTO.getAdminUserId()).orElse(null);
        if (adminUser == null) {
            throw new IllegalArgumentException("L'utilisateur admin spécifié n'existe pas");
        }

        // Vérifier si l'utilisateur a le rôle 'Admin'
        boolean isAdmin = adminUser.getRoles().stream()
                .anyMatch(role -> role.getRolename().equals("Admin"));
        if (!isAdmin) {
            throw new IllegalArgumentException("L'utilisateur doit avoir le rôle 'Admin'");
        }

        // Associer la Societe à l'utilisateur admin
        adminUser.setMySociete(societe);
        adminUser.setSociete(societe);
        societe.setAdminUser(adminUser);
        societe.getOperateurs().add(adminUser);

        // Enregistrer la société en base de données
        societeRepository.save(societe);

        // Mappage de Societe vers SocieteResponseDTO
        SocieteResponseDTO societeResponseDTO = societeMapper.modelMapper().map(societe, SocieteResponseDTO.class);

        return new ResponseEntity<>(societeResponseDTO, HttpStatus.CREATED);
    }






    @Override
    public ResponseEntity<SocieteResponseDTO> update(Long id, SocieteRequestDTO societeRequestDTO) {
        Optional<Societe> optionalSociete = societeRepository.findById(id);

        if (optionalSociete.isPresent()) {
            Societe societeToUpdate = optionalSociete.get();
            societeToUpdate.setAdresse(societeRequestDTO.getAdresse());
            societeToUpdate.setEmail(societeRequestDTO.getEmail());
            societeToUpdate.setNom(societeRequestDTO.getNom());
            societeToUpdate.setTelephone(societeRequestDTO.getTelephone());
            societeToUpdate.setMatriculeFiscal(societeRequestDTO.getMatriculeFiscal());



            AppUser adminUser = appUserRepository.findById(societeRequestDTO.getAdminUserId()).orElse(null);
            if (adminUser != null) {
                // Vérifier si l'utilisateur a le rôle 'Admin'
                boolean isAdmin = adminUser.getRoles().stream()
                        .anyMatch(role -> role.getRolename().equals("Admin"));
                if (!isAdmin) {
                    throw new IllegalArgumentException("L'utilisateur doit avoir le rôle 'Admin'");
                }
                adminUser.setSociete(societeToUpdate);
                societeToUpdate.setAdminUser(adminUser);
            }
            Abonnement abonnement= abonnementRepository.findById(societeRequestDTO.getAbonnementId()).orElse(null);
            Societe existingSociete = societeRepository.findByAbonnement(abonnement);
            if (existingSociete != null &&societeToUpdate!=existingSociete) {
                throw new IllegalStateException("Cet abonnement est déjà associé à une autre société");
            }
            societeToUpdate.setAbonnement(abonnement);

            Societe updatedSociete = societeRepository.save(societeToUpdate);
            SocieteResponseDTO updatedSocieteResponseDTO = societeMapper.modelMapper().map(updatedSociete, SocieteResponseDTO.class);
            return new ResponseEntity<>(updatedSocieteResponseDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Societe societe = societeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Societe not found with id " + id));
        if (societe != null) {
            societeRepository.delete(societe);
            return new ResponseEntity<>("Société supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Société non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
}

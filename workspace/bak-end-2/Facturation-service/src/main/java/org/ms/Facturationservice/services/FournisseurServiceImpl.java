package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.FournisseurRequestDto;
import org.ms.Facturationservice.dtos.FournisseurResponseDto;
import org.ms.Facturationservice.entities.Fournisseur;
import org.ms.Facturationservice.entities.LigneAchat;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.FournisseurMapperConfig;
import org.ms.Facturationservice.repositories.FournisseurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FournisseurServiceImpl implements FournisseurService {

    @Autowired
    private FournisseurRepository fournisseurRepository;

    @Autowired
    FournisseurMapperConfig fournisseurMapperConfig;

    @Autowired
    SocieteServiceFeign societeServiceFeign;
    @Override
    public List<FournisseurResponseDto> list() {
        List<Fournisseur> fournisseurs = fournisseurRepository.findAll();
        List<FournisseurResponseDto> fournisseurResponseDtos = new ArrayList<>();

        for (Fournisseur fournisseur : fournisseurs) {
            FournisseurResponseDto fournisseurResponseDto = fournisseurMapperConfig.modelMapper().map(fournisseur, FournisseurResponseDto.class);
            fournisseurResponseDto.setLigneAchatIds(fournisseur.getLigneAchats().stream().map(LigneAchat::getId).collect(Collectors.toList()));
            fournisseurResponseDtos.add(fournisseurResponseDto);
        }
        return fournisseurResponseDtos;
    }

    @Override
    public List<FournisseurResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {
            List<Fournisseur> fournisseurs = fournisseurRepository.findBySocieteId(societeId);
            List<FournisseurResponseDto> fournisseurResponseDtos = new ArrayList<>();

            for (Fournisseur fournisseur : fournisseurs) {
                FournisseurResponseDto fournisseurResponseDto = fournisseurMapperConfig.modelMapper().map(fournisseur, FournisseurResponseDto.class);
                fournisseurResponseDto.setLigneAchatIds(fournisseur.getLigneAchats().stream().map(LigneAchat::getId).collect(Collectors.toList()));
                fournisseurResponseDtos.add(fournisseurResponseDto);
            }
            return fournisseurResponseDtos;
        }
    }


    @Override
    public FournisseurResponseDto getOne(Long id) {
        Optional<Fournisseur> optionalFournisseur = fournisseurRepository.findById(id);
        Fournisseur fournisseur = optionalFournisseur.orElseThrow(() -> new EntityNotFoundException("Fournisseur not found"));

        List<Long> ligneAchatIds = fournisseur.getLigneAchats().stream().map(LigneAchat::getId).collect(Collectors.toList());

        FournisseurResponseDto fournisseurResponseDto = fournisseurMapperConfig.modelMapper().map(fournisseur, FournisseurResponseDto.class);
        fournisseurResponseDto.setLigneAchatIds(ligneAchatIds);

        return fournisseurResponseDto;
    }

    @Override
    public FournisseurResponseDto save(FournisseurRequestDto fournisseurRequestDto) {
        if (societeServiceFeign.getOne(fournisseurRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + fournisseurRequestDto.getSocieteId());
        } else {
            Fournisseur fournisseur = fournisseurMapperConfig.modelMapper().map(fournisseurRequestDto, Fournisseur.class);
            fournisseurRepository.save(fournisseur);
            return fournisseurMapperConfig.modelMapper().map(fournisseur, FournisseurResponseDto.class);
        }
    }


    @Override
    public FournisseurResponseDto update(Long id, FournisseurRequestDto fournisseurRequestDto) {
        if (societeServiceFeign.getOne(fournisseurRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + fournisseurRequestDto.getSocieteId());
        } else {
            Fournisseur fournisseurToUpdate = fournisseurRepository.findById(id).get();

            fournisseurToUpdate.setAdresse(fournisseurRequestDto.getAdresse());
            fournisseurToUpdate.setNom(fournisseurRequestDto.getNom());
            fournisseurToUpdate.setTelephone(fournisseurRequestDto.getTelephone());

            List<LigneAchat> ligneAchats = fournisseurToUpdate.getLigneAchats();

            fournisseurToUpdate.setLigneAchats(ligneAchats);


            Fournisseur updatedFournisseur = fournisseurRepository.save(fournisseurToUpdate);
            FournisseurResponseDto fournisseurResponseDto = fournisseurMapperConfig.modelMapper().map(updatedFournisseur, FournisseurResponseDto.class);
            List<Long> ligneAchatIds = updatedFournisseur.getLigneAchats().stream().map(LigneAchat::getId).collect(Collectors.toList());
            fournisseurResponseDto.setLigneAchatIds(ligneAchatIds);

            return fournisseurResponseDto;
        }
    }


    @Override
    public ResponseEntity<?> delete(Long id) {
        Fournisseur fournisseur = fournisseurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fournisseur not found with id " + id));
        if (fournisseur != null) {
            fournisseurRepository.delete(fournisseur);
            return new ResponseEntity<>("Fournisseur supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Fournisseur non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
}

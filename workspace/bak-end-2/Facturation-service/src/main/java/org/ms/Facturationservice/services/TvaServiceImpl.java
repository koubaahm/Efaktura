package org.ms.Facturationservice.services;




import org.ms.Facturationservice.dtos.TvaRequestDto;
import org.ms.Facturationservice.dtos.TvaResponseDto;
import org.ms.Facturationservice.entities.Produit;
import org.ms.Facturationservice.entities.Tva;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.TvaMapperConfig;
import org.ms.Facturationservice.repositories.TvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TvaServiceImpl implements TvaService {
    @Autowired
    private TvaRepository tvaRepository;
    @Autowired
    private TvaMapperConfig tvaMapperConfig;

    @Autowired
    SocieteServiceFeign societeServiceFeign;

    @Override
    public List<TvaResponseDto> list() {
        List<Tva> tvas = tvaRepository.findAll();
        List<TvaResponseDto> tvasDTO = new ArrayList<>();

        for (Tva tva : tvas) {
            TvaResponseDto tvaResponseDtoDTO = tvaMapperConfig.modelMapper().map(tva, TvaResponseDto.class);
            tvaResponseDtoDTO.setProduitIds(tva.getProduits().stream().map(Produit::getId).collect(Collectors.toList()));
            tvasDTO.add(tvaResponseDtoDTO);
        }


        return tvasDTO;
    }
    @Override
    public List <TvaResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {
        List<Tva> tvas = tvaRepository.findBySocieteId(societeId);
        List<TvaResponseDto> tvasDTO = new ArrayList<>();

        for (Tva tva : tvas) {
            TvaResponseDto tvaResponseDtoDTO = tvaMapperConfig.modelMapper().map(tva, TvaResponseDto.class);
            tvaResponseDtoDTO.setProduitIds(tva.getProduits().stream().map(Produit::getId).collect(Collectors.toList()));
            tvasDTO.add(tvaResponseDtoDTO);
        }

        return tvasDTO;
        }
    }



    @Override
    public TvaResponseDto getOne(Long id) {
        Optional<Tva> optionalTva = tvaRepository.findById(id);
        Tva tva = optionalTva.orElseThrow(() -> new EntityNotFoundException("Tva not found"));

        List<Long> produitIds = tva.getProduits().stream().map(Produit::getId).collect(Collectors.toList());

        TvaResponseDto tvaResponseDto = tvaMapperConfig.modelMapper().map(tva, TvaResponseDto.class);
        tvaResponseDto.setProduitIds(produitIds);

        return tvaResponseDto;
    }


    @Override
    public ResponseEntity<TvaResponseDto> save(TvaRequestDto tvaRequestDto) {
        if (societeServiceFeign.getOne(tvaRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + tvaRequestDto.getSocieteId());
        } else {
            Tva tva = tvaMapperConfig.modelMapper().map(tvaRequestDto, Tva.class); // convertir le DTO Tva en objet Tva

            tvaRepository.save(tva); // ajouter le tva à la base de données
            TvaResponseDto newTvaResponseDto = tvaMapperConfig.modelMapper().map(tva, TvaResponseDto.class); // convertir le nouveau Tva en DTO Tva

            return new ResponseEntity<>(newTvaResponseDto, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<TvaResponseDto> update(Long id, TvaRequestDto tvaRequestDto) {
        if (societeServiceFeign.getOne(tvaRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + tvaRequestDto.getSocieteId());
        } else {
        Optional<Tva> optionalTva = tvaRepository.findById(id);
        if (optionalTva.isPresent()) {
            Tva tva = optionalTva.get();
            tvaMapperConfig.modelMapper().map(tvaRequestDto, tva); // mettre à jour les champs du Tva avec les valeurs du DTO
            tvaRepository.save(tva);// mettre à jour le Tva dans la base de données
            TvaResponseDto updatedTvaResponseDto = tvaMapperConfig.modelMapper().map(tva, TvaResponseDto.class);// convertir le Tva mis à jour en DTO Tva
            return new ResponseEntity<>(updatedTvaResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Tva> optionalTva = tvaRepository.findById(id);

        if (optionalTva.isPresent()) { // si le Tva existe
            tvaRepository.deleteById(id); // supprimer le Tva
            return new ResponseEntity<>("Tva supprimée avec succès.", HttpStatus.OK);
        } else { // si le Tva n'existe pas
            return new ResponseEntity<>("Tva non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
}

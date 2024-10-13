package org.ms.Facturationservice.services;



import org.ms.Facturationservice.dtos.ProduitRequestDto;
import org.ms.Facturationservice.dtos.ProduitResponseDto;
import org.ms.Facturationservice.entities.Produit;
import org.ms.Facturationservice.entities.Tva;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.ProduitMapperConfig;
import org.ms.Facturationservice.repositories.ProduitRepository;
import org.ms.Facturationservice.repositories.TvaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProduitServiceImpl implements ProduitService {
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    ProduitMapperConfig produitMapperConfig;
    @Autowired
    TvaRepository tvaRepository;
    @Autowired
    SocieteServiceFeign societeServiceFeign;

    @Override
    public List<ProduitResponseDto> list() {
        List<Produit> produits = produitRepository.findAll();
        List<ProduitResponseDto> produitsDTO = new ArrayList<>();

        for (Produit produit : produits) {
            ProduitResponseDto produitResponseDto = produitMapperConfig.modelMapper().map(produit, ProduitResponseDto.class);
            produitResponseDto.setTvaId(produit.getTva().getId());
            produitsDTO.add(produitResponseDto);
        }

        return produitsDTO;
    }

    @Override
    public List<ProduitResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {
            List<Produit> produits = produitRepository.findBySocieteId(societeId);
            List<ProduitResponseDto> produitsDTO = new ArrayList<>();

            for (Produit produit : produits) {
                ProduitResponseDto produitResponseDto = produitMapperConfig.modelMapper().map(produit, ProduitResponseDto.class);
                produitResponseDto.setTvaId(produit.getTva().getId());
                produitsDTO.add(produitResponseDto);
            }

            return produitsDTO;
        }
    }


    @Override
    public ResponseEntity<ProduitResponseDto> getOne(Long id) {
        Optional<Produit> optionalProduit = produitRepository.findById(id);

        if (optionalProduit.isPresent()) {
            Produit produit = optionalProduit.get();
            ProduitResponseDto produitResponseDto = produitMapperConfig.modelMapper().map(produit, ProduitResponseDto.class);
            produitResponseDto.setTvaId(produit.getTva().getId()); // ajout de l'attribut TvaId
            return new ResponseEntity<>(produitResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


@Override
public ResponseEntity<ProduitResponseDto> save(ProduitRequestDto produitRequestDto) {
    if (societeServiceFeign.getOne(produitRequestDto.getSocieteId()) == null) {

        throw new RuntimeException("Société introuvable avec l'ID: " + produitRequestDto.getSocieteId());
    } else {
        Produit produit = produitMapperConfig.modelMapper().map(produitRequestDto, Produit.class);
        Tva tva = tvaRepository.findById(produitRequestDto.getTvaId()).orElse(null);
        produit.setTva(tva);
        produitRepository.save(produit);
        ProduitResponseDto produitResponseDto = produitMapperConfig.modelMapper().map(produit, ProduitResponseDto.class);
        assert tva != null;
        produitResponseDto.setTvaId(tva.getId());
        return new ResponseEntity<>(produitResponseDto, HttpStatus.CREATED);
    }

}


    @Override
    public ResponseEntity<ProduitResponseDto> update(Long id, ProduitRequestDto produitRequestDto) {
        if (societeServiceFeign.getOne(produitRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + produitRequestDto.getSocieteId());
        } else {
            Optional<Produit> optionalProduit = produitRepository.findById(id);
            if (optionalProduit.isPresent()) {
                Produit produit = optionalProduit.get();
                produitMapperConfig.modelMapper().map(produitRequestDto, produit); // mettre à jour les champs du Produit avec les valeurs du DTO

                // Mettre à jour la propriéttva en utilisant l'identifiant tva dans le ProduitRequestDto
                Long tvaId = produitRequestDto.getTvaId();
                Tva tva = tvaRepository.findById(tvaId).orElse(null);
                produit.setTva(tva);

                produitRepository.save(produit);// mettre à jour le Produit dans la base de données
                ProduitResponseDto updatedProduitResponseDto = produitMapperConfig.modelMapper().map(produit, ProduitResponseDto.class);// convertir le Produit mis à jour en DTO Produit
                if (produit.getTva() != null) {
                    updatedProduitResponseDto.setTvaId(tva.getId());
                }
                return new ResponseEntity<>(updatedProduitResponseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Produit> optionalProduit = produitRepository.findById(id);

        if (optionalProduit.isPresent()) { // si le Produit existe
            produitRepository.deleteById(id); // supprimer le Produit
            return new ResponseEntity<>("Produit supprimée avec succès.", HttpStatus.OK);
        } else { // si le Produit n'existe pas
            return new ResponseEntity<>("Produit non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
}

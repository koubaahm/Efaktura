package org.ms.Facturationservice.services;


import org.ms.Facturationservice.dtos.LigneAchatRequestDto;
import org.ms.Facturationservice.dtos.LigneAchatResponseDto;
import org.ms.Facturationservice.entities.*;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.LigneAchatMapperConfig;
import org.ms.Facturationservice.repositories.FournisseurRepository;
import org.ms.Facturationservice.repositories.LigneAchatRepository;
import org.ms.Facturationservice.repositories.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
@Service
@Transactional
public class LigneAchatServiceImpl implements LigneAchatService{

    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private LigneAchatRepository ligneAchatRepository;
    @Autowired
    private FournisseurRepository fournisseurRepository;
    @Autowired
    private LigneAchatMapperConfig ligneAchatMapperConfig;
    @Autowired
    SocieteServiceFeign societeServiceFeign;
    @Override
    public List<LigneAchatResponseDto> list() {
        List<LigneAchat> ligneAchats = ligneAchatRepository.findAll();
        List<LigneAchatResponseDto> ligneAchatResponseDtos= new ArrayList<>();

        for (LigneAchat ligneAchat : ligneAchats) {
            LigneAchatResponseDto ligneAchatResponseDto = ligneAchatMapperConfig.modelMapper().map(ligneAchat, LigneAchatResponseDto.class);
            ligneAchatResponseDto.setFournisseurId(ligneAchat.getFournisseur().getId());
            ligneAchatResponseDto.setFournisseurNom(ligneAchat.getFournisseur().getNom());
            ligneAchatResponseDto.setQuantiteStock(ligneAchat.getQuantiteStock());
            ligneAchatResponseDto.setQuantiteAchat(ligneAchatResponseDto.getQuantiteAchat());
            ligneAchatResponseDtos.add(ligneAchatResponseDto);
        }
        return ligneAchatResponseDtos;
    }

    @Override
    public List<LigneAchatResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {


            List<LigneAchat> ligneAchats = ligneAchatRepository.findBySocieteId(societeId);
            List<LigneAchatResponseDto> ligneAchatResponseDtos = new ArrayList<>();

            for (LigneAchat ligneAchat : ligneAchats) {
                LigneAchatResponseDto ligneAchatResponseDto = ligneAchatMapperConfig.modelMapper().map(ligneAchat, LigneAchatResponseDto.class);
                ligneAchatResponseDto.setFournisseurId(ligneAchat.getFournisseur().getId());
                ligneAchatResponseDto.setFournisseurNom(ligneAchat.getFournisseur().getNom());
                ligneAchatResponseDto.setQuantiteStock(ligneAchat.getQuantiteStock());
                ligneAchatResponseDto.setQuantiteAchat(ligneAchat.getQuantiteAchat());
                ligneAchatResponseDtos.add(ligneAchatResponseDto);
            }
            return ligneAchatResponseDtos;
        }
    }

    @Override
    public List<LigneAchatResponseDto> listByProduitId(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("produit introuvable avec l'ID: " + produitId));
        List<LigneAchat> ligneAchats = ligneAchatRepository.findByProduit(produit);
        List<LigneAchatResponseDto> ligneAchatResponseDtos = new ArrayList<>();

        for (LigneAchat ligneAchat : ligneAchats) {
            LigneAchatResponseDto ligneAchatResponseDto = ligneAchatMapperConfig.modelMapper().map(ligneAchat, LigneAchatResponseDto.class);
            ligneAchatResponseDto.setDate(ligneAchat.getDate());
            ligneAchatResponseDto.setProduitId(ligneAchat.getProduit().getId());
            ligneAchatResponseDto.setSocieteId(ligneAchat.getSocieteId());
            ligneAchatResponseDto.setQuantiteAchat(ligneAchat.getQuantiteAchat());
            ligneAchatResponseDto.setId(ligneAchat.getId());
            ligneAchatResponseDto.setQuantiteStock(ligneAchat.getQuantiteStock());
            ligneAchatResponseDto.setPrixUnitaire(ligneAchat.getPrixUnitaire());
            ligneAchatResponseDto.setFournisseurId(ligneAchat.getFournisseur().getId());
            ligneAchatResponseDto.setFournisseurNom(ligneAchat.getFournisseur().getNom());
            ligneAchatResponseDto.setPrixTotal(ligneAchat.getPrixTotal());


            ligneAchatResponseDtos.add(ligneAchatResponseDto);
        }

        return ligneAchatResponseDtos;
    }


    @Override
    public LigneAchatResponseDto getOne(Long id) {
        LigneAchat ligneAchat= ligneAchatRepository.findById(id).orElse(null);
        assert ligneAchat != null;
        Fournisseur fournisseur=ligneAchat.getFournisseur();
        Produit produit=ligneAchat.getProduit();
        LigneAchatResponseDto ligneAchatResponseDto= ligneAchatMapperConfig.modelMapper().map(ligneAchat, LigneAchatResponseDto.class);
        ligneAchatResponseDto.setFournisseurId(fournisseur.getId());
        ligneAchatResponseDto.setFournisseurNom(fournisseur.getNom());
        ligneAchatResponseDto.setQuantiteAchat(ligneAchat.getQuantiteAchat());
        ligneAchatResponseDto.setQuantiteStock(ligneAchat.getQuantiteStock());
        ligneAchatResponseDto.setProduitId(produit.getId());
        return ligneAchatResponseDto;
    }

    @Override
    public LigneAchatResponseDto save(LigneAchatRequestDto ligneAchatRequestDto) {
        if (societeServiceFeign.getOne(ligneAchatRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + ligneAchatRequestDto.getSocieteId());
        } else {
            LigneAchat ligneAchat = new LigneAchat();
            ligneAchat.setQuantiteAchat(ligneAchatRequestDto.getQuantiteAchat());
            ligneAchat.setPrixUnitaire(ligneAchatRequestDto.getPrixUnitaire());
            ligneAchat.setSocieteId(ligneAchatRequestDto.getSocieteId());


            Fournisseur fournisseur = fournisseurRepository.findById(ligneAchatRequestDto.getFournisseurId())
                    .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + ligneAchatRequestDto.getFournisseurId()));
            Produit produit = produitRepository.findById(ligneAchatRequestDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneAchatRequestDto.getProduitId()));
            produit.setQuantite(produit.getQuantite() + ligneAchatRequestDto.getQuantiteAchat());
            ligneAchat.setQuantiteStock(ligneAchatRequestDto.getQuantiteAchat());
            produitRepository.save(produit);
            ligneAchat.setProduit(produit);
            ligneAchat.setFournisseur(fournisseur);


            LigneAchat savedLigneAchat = ligneAchatRepository.save(ligneAchat);

            LigneAchatResponseDto ligneAchatResponseDto = new LigneAchatResponseDto();
            ligneAchatResponseDto.setId(savedLigneAchat.getId());
            ligneAchatResponseDto.setDate(ligneAchat.getDate());
            ligneAchatResponseDto.setQuantiteAchat(savedLigneAchat.getQuantiteAchat());
            ligneAchatResponseDto.setQuantiteStock(savedLigneAchat.getQuantiteStock());
            ligneAchatResponseDto.setPrixUnitaire(savedLigneAchat.getPrixUnitaire());
            ligneAchatResponseDto.setProduitId(savedLigneAchat.getProduit().getId());
            ligneAchatResponseDto.setFournisseurId(savedLigneAchat.getFournisseur().getId());
            ligneAchatResponseDto.setFournisseurNom(savedLigneAchat.getFournisseur().getNom());
            ligneAchatResponseDto.setSocieteId(savedLigneAchat.getSocieteId());
            ligneAchatResponseDto.setPrixTotal(ligneAchat.getPrixTotal());

            return ligneAchatResponseDto;
        }
    }

    @Override
    public List<LigneAchatResponseDto> saveList(ArrayList<LigneAchatRequestDto> ligneAchatRequestDtoList) {
        List<LigneAchatResponseDto> ligneAchatResponseDtoList = new ArrayList<>();

        for (LigneAchatRequestDto ligneAchatRequestDto : ligneAchatRequestDtoList) {
            if (societeServiceFeign.getOne(ligneAchatRequestDto.getSocieteId()) == null) {
                throw new RuntimeException("Société introuvable avec l'ID: " + ligneAchatRequestDto.getSocieteId());
            } else {
                LigneAchat ligneAchat = new LigneAchat();
                ligneAchat.setQuantiteAchat(ligneAchatRequestDto.getQuantiteAchat());
                ligneAchat.setPrixUnitaire(ligneAchatRequestDto.getPrixUnitaire());
                ligneAchat.setSocieteId(ligneAchatRequestDto.getSocieteId());

                Fournisseur fournisseur = fournisseurRepository.findById(ligneAchatRequestDto.getFournisseurId())
                        .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + ligneAchatRequestDto.getFournisseurId()));
                Produit produit = produitRepository.findById(ligneAchatRequestDto.getProduitId())
                        .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneAchatRequestDto.getProduitId()));
                produit.setQuantite(produit.getQuantite() + ligneAchatRequestDto.getQuantiteAchat());
                ligneAchat.setQuantiteStock(ligneAchatRequestDto.getQuantiteAchat());
                produitRepository.save(produit);
                ligneAchat.setProduit(produit);
                ligneAchat.setFournisseur(fournisseur);

                LigneAchat savedLigneAchat = ligneAchatRepository.save(ligneAchat);

                LigneAchatResponseDto ligneAchatResponseDto = new LigneAchatResponseDto();
                ligneAchatResponseDto.setId(savedLigneAchat.getId());
                ligneAchatResponseDto.setDate(ligneAchat.getDate());
                ligneAchatResponseDto.setQuantiteAchat(savedLigneAchat.getQuantiteAchat());
                ligneAchatResponseDto.setQuantiteStock(savedLigneAchat.getQuantiteStock());
                ligneAchatResponseDto.setPrixUnitaire(savedLigneAchat.getPrixUnitaire());
                ligneAchatResponseDto.setProduitId(savedLigneAchat.getProduit().getId());
                ligneAchatResponseDto.setFournisseurId(savedLigneAchat.getFournisseur().getId());
                ligneAchatResponseDto.setFournisseurNom(savedLigneAchat.getFournisseur().getNom());
                ligneAchatResponseDto.setSocieteId(savedLigneAchat.getSocieteId());
                ligneAchatResponseDto.setPrixTotal(ligneAchat.getPrixTotal());

                ligneAchatResponseDtoList.add(ligneAchatResponseDto);
            }
        }

        return ligneAchatResponseDtoList;
    }


    @Override
    public LigneAchatResponseDto update(Long id, LigneAchatRequestDto ligneAchatRequestDto) {
        if (societeServiceFeign.getOne(ligneAchatRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + ligneAchatRequestDto.getSocieteId());
        } else {
            LigneAchat ligneAchatToUpdate = ligneAchatRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("ligneAchat introuvable avec l'ID: " + id));

            ligneAchatToUpdate.setQuantiteAchat(ligneAchatRequestDto.getQuantiteAchat());
            ligneAchatToUpdate.setPrixUnitaire(ligneAchatRequestDto.getPrixUnitaire());
            ligneAchatToUpdate.setSocieteId(ligneAchatRequestDto.getSocieteId());
            ligneAchatToUpdate.setQuantiteStock(ligneAchatRequestDto.getQuantiteAchat());
            Fournisseur fournisseur = fournisseurRepository.findById(ligneAchatRequestDto.getFournisseurId())
                    .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + ligneAchatRequestDto.getFournisseurId()));
            ligneAchatToUpdate.setFournisseur(fournisseur);

            Produit produit = produitRepository.findById(ligneAchatRequestDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneAchatRequestDto.getProduitId()));

            produit.setQuantite(produit.getQuantite() + ligneAchatRequestDto.getQuantiteAchat());

            produitRepository.save(produit);

            ligneAchatToUpdate.setProduit(produit);

            LigneAchat updatedLigneAchat = ligneAchatRepository.save(ligneAchatToUpdate);

            LigneAchatResponseDto ligneAchatResponseDto = new LigneAchatResponseDto();
            ligneAchatResponseDto.setId(updatedLigneAchat.getId());
            ligneAchatResponseDto.setDate(updatedLigneAchat.getDate());
            ligneAchatResponseDto.setQuantiteAchat(updatedLigneAchat.getQuantiteAchat());
            ligneAchatResponseDto.setQuantiteStock(updatedLigneAchat.getQuantiteStock());
            ligneAchatResponseDto.setPrixUnitaire(updatedLigneAchat.getPrixUnitaire());
            ligneAchatResponseDto.setProduitId(updatedLigneAchat.getProduit().getId());
            ligneAchatResponseDto.setFournisseurId(updatedLigneAchat.getFournisseur().getId());
            ligneAchatResponseDto.setSocieteId(updatedLigneAchat.getSocieteId());
            ligneAchatResponseDto.setPrixTotal(updatedLigneAchat.getPrixTotal());
            ligneAchatResponseDto.setFournisseurNom(updatedLigneAchat.getFournisseur().getNom());


            return ligneAchatResponseDto;
        }
    }
    @Override
    public List<LigneAchatResponseDto> updateList(List<LigneAchatRequestDto> ligneAchatRequestDtoList) {
        List<LigneAchatResponseDto> ligneAchatResponseDtoList = new ArrayList<>();

        for (LigneAchatRequestDto ligneAchatRequestDto : ligneAchatRequestDtoList) {
            if (societeServiceFeign.getOne(ligneAchatRequestDto.getSocieteId()) == null) {
                throw new RuntimeException("Société introuvable avec l'ID: " + ligneAchatRequestDto.getSocieteId());
            } else {
                LigneAchat ligneAchatToUpdate = ligneAchatRepository.findById(ligneAchatRequestDto.getId())
                        .orElseThrow(() -> new RuntimeException("ligneAchat introuvable avec l'ID: " + ligneAchatRequestDto.getId()));

                ligneAchatToUpdate.setQuantiteAchat(ligneAchatRequestDto.getQuantiteAchat());
                ligneAchatToUpdate.setPrixUnitaire(ligneAchatRequestDto.getPrixUnitaire());
                ligneAchatToUpdate.setSocieteId(ligneAchatRequestDto.getSocieteId());
                ligneAchatToUpdate.setQuantiteStock(ligneAchatRequestDto.getQuantiteAchat());

                Fournisseur fournisseur = fournisseurRepository.findById(ligneAchatRequestDto.getFournisseurId())
                        .orElseThrow(() -> new RuntimeException("Fournisseur introuvable avec l'ID: " + ligneAchatRequestDto.getFournisseurId()));
                ligneAchatToUpdate.setFournisseur(fournisseur);

                Produit produit = produitRepository.findById(ligneAchatRequestDto.getProduitId())
                        .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneAchatRequestDto.getProduitId()));

                produit.setQuantite(produit.getQuantite() + ligneAchatRequestDto.getQuantiteAchat());
                produitRepository.save(produit);

                ligneAchatToUpdate.setProduit(produit);

                LigneAchat updatedLigneAchat = ligneAchatRepository.save(ligneAchatToUpdate);

                LigneAchatResponseDto ligneAchatResponseDto = new LigneAchatResponseDto();
                ligneAchatResponseDto.setId(updatedLigneAchat.getId());
                ligneAchatResponseDto.setDate(updatedLigneAchat.getDate());
                ligneAchatResponseDto.setQuantiteAchat(updatedLigneAchat.getQuantiteAchat());
                ligneAchatResponseDto.setQuantiteStock(updatedLigneAchat.getQuantiteStock());
                ligneAchatResponseDto.setPrixUnitaire(updatedLigneAchat.getPrixUnitaire());
                ligneAchatResponseDto.setProduitId(updatedLigneAchat.getProduit().getId());
                ligneAchatResponseDto.setFournisseurId(updatedLigneAchat.getFournisseur().getId());
                ligneAchatResponseDto.setSocieteId(updatedLigneAchat.getSocieteId());
                ligneAchatResponseDto.setPrixTotal(updatedLigneAchat.getPrixTotal());
                ligneAchatResponseDto.setFournisseurNom(updatedLigneAchat.getFournisseur().getNom());

                ligneAchatResponseDtoList.add(ligneAchatResponseDto);
            }
        }

        return ligneAchatResponseDtoList;
    }



    @Override
    public ResponseEntity<?> deleteLigneAchat(Long id) {
        LigneAchat ligneAchat= ligneAchatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LigneAchat not found with id " + id));
        if (ligneAchat != null) {
            ligneAchatRepository.delete(ligneAchat);
            return new ResponseEntity<>("LigneAchat supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("LigneAchat non trouvée.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public List<LigneAchatResponseDto> getAllByProduit(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + produitId));
        List<LigneAchat> lignesAchat = ligneAchatRepository.findByProduit(produit);

        List<LigneAchatResponseDto> ligneAchatResponseDtos = new ArrayList<>();

        for (LigneAchat ligneAchat : lignesAchat) {
            LigneAchatResponseDto ligneAchatResponseDto = new LigneAchatResponseDto();
            ligneAchatResponseDto.setId(ligneAchat.getId());
            ligneAchatResponseDto.setDate(ligneAchat.getDate());
            ligneAchatResponseDto.setProduitId(ligneAchat.getProduit().getId());
            ligneAchatResponseDto.setFournisseurId(ligneAchat.getFournisseur().getId());
            ligneAchatResponseDto.setFournisseurNom(ligneAchat.getFournisseur().getNom());
            ligneAchatResponseDto.setQuantiteAchat(ligneAchat.getQuantiteAchat());
            ligneAchatResponseDto.setPrixUnitaire(ligneAchat.getPrixUnitaire());
            ligneAchatResponseDto.setQuantiteStock(ligneAchat.getQuantiteStock());
            ligneAchatResponseDtos.add(ligneAchatResponseDto);
        }

        return ligneAchatResponseDtos;
    }



}

package org.ms.Facturationservice.services;



import org.ms.Facturationservice.dtos.LigneFactureRequestDto;
import org.ms.Facturationservice.dtos.LigneFactureResponseDto;
import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.entities.LigneAchat;
import org.ms.Facturationservice.entities.LigneFacture;
import org.ms.Facturationservice.entities.Produit;
import org.ms.Facturationservice.mapper.LigneFactureMapperConfig;
import org.ms.Facturationservice.repositories.FactureRepository;
import org.ms.Facturationservice.repositories.LigneAchatRepository;
import org.ms.Facturationservice.repositories.LigneFactureRepository;
import org.ms.Facturationservice.repositories.ProduitRepository;
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
public class LigneFactureServiceImpl implements LigneFactureService {
    @Autowired
    LigneFactureRepository ligneFactureRepository;
    @Autowired
    private FactureRepository factureRepository;
    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    LigneFactureMapperConfig ligneFactureMapperConfig;

    @Autowired
    LigneAchatRepository ligneAchatRepository;

    @Override
    public List<LigneFactureResponseDto> list() {
        List<LigneFacture> ligneFactures = ligneFactureRepository.findAll();
        List<LigneFactureResponseDto> ligneFactureResponseDtos = new ArrayList<>();

        for (LigneFacture ligneFacture : ligneFactures) {
            LigneFactureResponseDto ligneFactureResponseDto = ligneFactureMapperConfig.modelMapper().map(ligneFacture, LigneFactureResponseDto.class);
            ligneFactureResponseDto.setFactureId(ligneFacture.getFacture().getId());
            ligneFactureResponseDto.setRemise(ligneFacture.getRemise());
            ligneFactureResponseDto.setIdLigneAchat(ligneFacture.getIdLigneAchat());
            ligneFactureResponseDtos.add(ligneFactureResponseDto);
        }
        return ligneFactureResponseDtos;
    }
    @Override
    public List<LigneFactureResponseDto> listByFactureId(Long factureId) {
        Facture facture = factureRepository.findById(factureId)
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'ID: " + factureId));
        List<LigneFacture> ligneFactures = ligneFactureRepository.findByFacture(facture);
        List<LigneFactureResponseDto> ligneFactureResponseDtos = new ArrayList<>();

        for (LigneFacture ligneFacture : ligneFactures) {
            LigneFactureResponseDto ligneFactureResponseDto = ligneFactureMapperConfig.modelMapper().map(ligneFacture, LigneFactureResponseDto.class);
            ligneFactureResponseDto.setFactureId(ligneFacture.getFacture().getId());
            ligneFactureResponseDto.setRemise(ligneFacture.getRemise());
            ligneFactureResponseDto.setIdLigneAchat(ligneFacture.getIdLigneAchat());
            ligneFactureResponseDtos.add(ligneFactureResponseDto);
        }

        return ligneFactureResponseDtos;
    }


    @Override
    public LigneFactureResponseDto getOne(Long id) {
        LigneFacture ligneFacture = ligneFactureRepository.findById(id).orElse(null);
        assert ligneFacture != null;
        Facture facture = ligneFacture.getFacture();
        Produit produit = ligneFacture.getProduit();
        LigneFactureResponseDto ligneFactureResponseDto = ligneFactureMapperConfig.modelMapper().map(ligneFacture, LigneFactureResponseDto.class);
        ligneFactureResponseDto.setFactureId(facture.getId());
        ligneFactureResponseDto.setProduitId(produit.getId());
        ligneFactureResponseDto.setRemise(ligneFacture.getRemise());
        ligneFactureResponseDto.setIdLigneAchat(ligneFacture.getIdLigneAchat());
        return ligneFactureResponseDto;
    }




    @Override
    public LigneFactureResponseDto save(LigneFactureRequestDto ligneFactureRequestDto) {
        LigneFacture ligneFacture = new LigneFacture();

        ligneFacture.setTauxRemise(ligneFactureRequestDto.getTauxRemise());
        ligneFacture.setPrixUnitaire(ligneFactureRequestDto.getPrixUnitaire());


        Facture facture = factureRepository.findById(ligneFactureRequestDto.getFactureId())
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'ID: " + ligneFactureRequestDto.getFactureId()));

        ligneFacture.setFacture(facture);


        Produit produit = produitRepository.findById(ligneFactureRequestDto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneFactureRequestDto.getProduitId()));

        ligneFacture.setProduit(produit);



        LigneAchat ligneAchat = ligneAchatRepository.findById(ligneFactureRequestDto.getIdLigneAchat())
                .orElseThrow(() -> new RuntimeException("Ligne d'achat introuvable avec l'ID: " + ligneFactureRequestDto.getIdLigneAchat()));

        if (!ligneAchat.getProduit().equals(produit)) {
            throw new RuntimeException("Le produit de la ligne d'achat ne correspond pas au produit spécifié.");
        }


        if (ligneAchat.getQuantiteStock() < ligneFactureRequestDto.getQuantite()) {
            throw new RuntimeException("La quantité en stock est insuffisante pour effectuer cette vente.");
        }
        ligneFacture.setIdLigneAchat(ligneFactureRequestDto.getIdLigneAchat());
        produit.setQuantite(produit.getQuantite() - ligneFacture.getQuantite());
        produitRepository.save(produit);
        ligneAchat.setQuantiteStock(ligneAchat.getQuantiteStock() - ligneFacture.getQuantite());
        ligneAchatRepository.save(ligneAchat);
        ligneFacture.setQuantite(ligneFactureRequestDto.getQuantite());

        ligneFactureRepository.save(ligneFacture);

        LigneFactureResponseDto ligneFactureResponseDto = new LigneFactureResponseDto();
        ligneFactureResponseDto.setId(ligneFacture.getId());
        ligneFactureResponseDto.setQuantite(ligneFacture.getQuantite());
        ligneFactureResponseDto.setPrixUnitaire(ligneFacture.getPrixUnitaire());
        ligneFactureResponseDto.setTauxRemise(ligneFacture.getTauxRemise());
        ligneFactureResponseDto.setRemise(ligneFacture.getRemise());
        ligneFactureResponseDto.setPrixVente(ligneFacture.getPrixVente());
        ligneFactureResponseDto.setProduitId(ligneFacture.getProduit().getId());
        ligneFactureResponseDto.setFactureId(ligneFacture.getFacture().getId());
        ligneFactureResponseDto.setIdLigneAchat(ligneFacture.getIdLigneAchat());
        ligneFactureResponseDto.setTotal(ligneFacture.getTotal());

        return ligneFactureResponseDto;
    }





    @Override
    public List<LigneFactureResponseDto> saveList(ArrayList<LigneFactureRequestDto> ligneFactureRequestDtos) {
        List<LigneFactureResponseDto> ligneFactureResponseDtos = new ArrayList<>();

        for (int i = 0; i < ligneFactureRequestDtos.size(); i++) {
            LigneFactureRequestDto ligneFactureRequestDto = ligneFactureRequestDtos.get(i);


            LigneFacture ligneFacture = new LigneFacture();
            ligneFacture.setTauxRemise(ligneFactureRequestDto.getTauxRemise());


            Facture facture = factureRepository.findById(ligneFactureRequestDto.getFactureId())
                    .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'ID: " + ligneFactureRequestDto.getFactureId()));
            Produit produit = produitRepository.findById(ligneFactureRequestDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneFactureRequestDto.getProduitId()));
            LigneAchat ligneAchat = ligneAchatRepository.findById(ligneFactureRequestDto.getIdLigneAchat())
                    .orElseThrow(() -> new RuntimeException("Ligne d'achat introuvable avec l'ID: " + ligneFactureRequestDto.getIdLigneAchat()));

            if (!ligneAchat.getProduit().equals(produit)) {
                throw new RuntimeException("Le produit de la ligne d'achat ne correspond pas au produit spécifié.");
            }

            if (ligneAchat.getQuantiteStock() < ligneFactureRequestDto.getQuantite()) {
                throw new RuntimeException("La quantité en stock est insuffisante pour effectuer cette vente.");
            }
            ligneFacture.setIdLigneAchat(ligneFactureRequestDto.getIdLigneAchat());
            ligneFacture.setQuantite(ligneFactureRequestDto.getQuantite());
            ligneFacture.setPrixUnitaire(ligneFactureRequestDto.getPrixUnitaire());
            produit.setQuantite(produit.getQuantite() - ligneFacture.getQuantite());
            ligneAchat.setQuantiteStock(ligneAchat.getQuantiteStock() - ligneFacture.getQuantite());
            produitRepository.save(produit);
            ligneAchatRepository.save(ligneAchat);

            ligneFacture.setProduit(produit);
            ligneFacture.setFacture(facture);

            ligneFactureRepository.save(ligneFacture);

            LigneFactureResponseDto ligneFactureResponseDto = new LigneFactureResponseDto();
            ligneFactureResponseDto.setId(ligneFacture.getId());
            ligneFactureResponseDto.setQuantite(ligneFacture.getQuantite());
            ligneFactureResponseDto.setPrixUnitaire(ligneFacture.getPrixUnitaire());
            ligneFactureResponseDto.setTauxRemise(ligneFacture.getTauxRemise());
            ligneFactureResponseDto.setRemise(ligneFacture.getRemise());
            ligneFactureResponseDto.setPrixVente(ligneFacture.getPrixVente());
            ligneFactureResponseDto.setProduitId(ligneFacture.getProduit().getId());
            ligneFactureResponseDto.setFactureId(ligneFacture.getFacture().getId());
            ligneFactureResponseDto.setIdLigneAchat(ligneFacture.getIdLigneAchat());
            ligneFactureResponseDto.setTotal(ligneFacture.getTotal());

            ligneFactureResponseDtos.add(ligneFactureResponseDto);
        }

        return ligneFactureResponseDtos;
    }




    @Override
    public LigneFactureResponseDto update(Long id, LigneFactureRequestDto ligneFactureRequestDto) {
        LigneFacture ligneFactureToUpdate = ligneFactureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture ligne introuvable avec l'ID: " + id));

        ligneFactureToUpdate.setPrixUnitaire(ligneFactureRequestDto.getPrixUnitaire());
        ligneFactureToUpdate.setTauxRemise(ligneFactureRequestDto.getTauxRemise());

        Facture facture = factureRepository.findById(ligneFactureRequestDto.getFactureId())
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'ID: " + ligneFactureRequestDto.getFactureId()));

        ligneFactureToUpdate.setFacture(facture);

        Produit produit = produitRepository.findById(ligneFactureRequestDto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneFactureRequestDto.getProduitId()));

        ligneFactureToUpdate.setProduit(produit);

        LigneAchat ligneAchat = ligneAchatRepository.findById(ligneFactureRequestDto.getIdLigneAchat())
                .orElseThrow(() -> new RuntimeException("Ligne d'achat introuvable avec l'ID: " + ligneFactureRequestDto.getIdLigneAchat()));

        if (!ligneAchat.getProduit().equals(produit)) {
            throw new RuntimeException("Le produit de la ligne d'achat ne correspond pas au produit spécifié.");
        }


        if (ligneAchat.getQuantiteStock() < ligneFactureRequestDto.getQuantite()) {

            throw new RuntimeException("La quantité en stock est insuffisante pour effectuer cette vente.");
        }

        ligneFactureToUpdate.setQuantite(ligneFactureRequestDto.getQuantite());
        ligneFactureToUpdate.setIdLigneAchat(ligneFactureRequestDto.getIdLigneAchat());

        produit.setQuantite(produit.getQuantite() - ligneFactureRequestDto.getQuantite());

        produitRepository.save(produit);

        ligneAchat.setQuantiteStock(ligneAchat.getQuantiteStock() - ligneFactureRequestDto.getQuantite());

        ligneAchatRepository.save(ligneAchat);


        ligneFactureRepository.save(ligneFactureToUpdate);

        LigneFactureResponseDto ligneFactureResponseDto = new LigneFactureResponseDto();
        ligneFactureResponseDto.setId(ligneFactureToUpdate.getId());
        ligneFactureResponseDto.setQuantite(ligneFactureToUpdate.getQuantite());
        ligneFactureResponseDto.setPrixUnitaire(ligneFactureToUpdate.getPrixUnitaire());
        ligneFactureResponseDto.setTauxRemise(ligneFactureToUpdate.getTauxRemise());
        ligneFactureResponseDto.setRemise(ligneFactureToUpdate.getRemise());
        ligneFactureResponseDto.setPrixVente(ligneFactureToUpdate.getPrixVente());
        ligneFactureResponseDto.setProduitId(ligneFactureToUpdate.getProduit().getId());
        ligneFactureResponseDto.setFactureId(ligneFactureToUpdate.getFacture().getId());
        ligneFactureResponseDto.setIdLigneAchat(ligneFactureToUpdate.getIdLigneAchat());
        ligneFactureResponseDto.setTotal(ligneFactureToUpdate.getTotal());

        return ligneFactureResponseDto;
    }


@Override
public List<LigneFactureResponseDto> updateList(List<LigneFactureRequestDto> ligneFactureRequestDtos) {
    List<LigneFactureResponseDto> updatedLigneFactures = new ArrayList<>();

    for (int i = 0; i < ligneFactureRequestDtos.size(); i++) {
        LigneFactureRequestDto ligneFactureRequestDto = ligneFactureRequestDtos.get(i);


        Long id = ligneFactureRequestDto.getId();
        LigneFacture ligneFactureToUpdate = ligneFactureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture ligne introuvable avec l'ID: " + id));

        ligneFactureToUpdate.setPrixUnitaire(ligneFactureRequestDto.getPrixUnitaire());
        ligneFactureToUpdate.setTauxRemise(ligneFactureRequestDto.getTauxRemise());


        Facture facture = factureRepository.findById(ligneFactureRequestDto.getFactureId())
                .orElseThrow(() -> new RuntimeException("Facture introuvable avec l'ID: " + ligneFactureRequestDto.getFactureId()));

        ligneFactureToUpdate.setFacture(facture);

        Produit produit = produitRepository.findById(ligneFactureRequestDto.getProduitId())
                .orElseThrow(() -> new RuntimeException("Produit introuvable avec l'ID: " + ligneFactureRequestDto.getProduitId()));

        ligneFactureToUpdate.setProduit(produit);


        LigneAchat ligneAchat = ligneAchatRepository.findById(ligneFactureRequestDto.getIdLigneAchat())
                .orElseThrow(() -> new RuntimeException("Ligne d'achat introuvable avec l'ID: " + ligneFactureRequestDto.getIdLigneAchat()));

        if (!ligneAchat.getProduit().equals(produit)) {
            throw new RuntimeException("Le produit de la ligne d'achat ne correspond pas au produit spécifié.");
        }

        if(ligneFactureToUpdate.getQuantite()!=ligneFactureRequestDto.getQuantite()){
            if (ligneAchat.getQuantiteStock() < ligneFactureRequestDto.getQuantite()) {
                throw new RuntimeException("La quantité en stock est insuffisante pour effectuer cette vente.");
            }
            ligneFactureToUpdate.setIdLigneAchat(ligneFactureRequestDto.getIdLigneAchat());
            produit.setQuantite(produit.getQuantite()+ ligneFactureToUpdate.getQuantite()- ligneFactureRequestDto.getQuantite());
            ligneAchat.setQuantiteStock(ligneAchat.getQuantiteStock() +ligneFactureToUpdate.getQuantite()- ligneFactureRequestDto.getQuantite());
            ligneAchatRepository.save(ligneAchat);
            ligneFactureToUpdate.setQuantite(ligneFactureRequestDto.getQuantite());




            produitRepository.save(produit);
            ligneFactureRepository.save(ligneFactureToUpdate);
        }


        LigneFactureResponseDto ligneFactureResponseDto = new LigneFactureResponseDto();
        ligneFactureResponseDto.setId(ligneFactureToUpdate.getId());
        ligneFactureResponseDto.setQuantite(ligneFactureToUpdate.getQuantite());
        ligneFactureResponseDto.setPrixUnitaire(ligneFactureToUpdate.getPrixUnitaire());
        ligneFactureResponseDto.setTauxRemise(ligneFactureToUpdate.getTauxRemise());
        ligneFactureResponseDto.setRemise(ligneFactureToUpdate.getRemise());
        ligneFactureResponseDto.setPrixVente(ligneFactureToUpdate.getPrixVente());
        ligneFactureResponseDto.setProduitId(ligneFactureToUpdate.getProduit().getId());
        ligneFactureResponseDto.setFactureId(ligneFactureToUpdate.getFacture().getId());
        ligneFactureResponseDto.setIdLigneAchat(ligneFactureToUpdate.getIdLigneAchat());
        ligneFactureResponseDto.setTotal(ligneFactureToUpdate.getTotal());

        updatedLigneFactures.add(ligneFactureResponseDto);
    }

    return updatedLigneFactures;
}





    @Override
    public ResponseEntity<?> deleteLigneFacture(Long id) {
        LigneFacture ligneFacture = ligneFactureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LigneFacture not found with id " + id));
        if (ligneFacture != null) {
            ligneFactureRepository.delete(ligneFacture);
            return new ResponseEntity<>("LigneFacture supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("LigneFacture non trouvée.", HttpStatus.NOT_FOUND);
        }
    }


}

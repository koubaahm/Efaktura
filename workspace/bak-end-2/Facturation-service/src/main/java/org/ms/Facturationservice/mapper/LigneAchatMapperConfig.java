package org.ms.Facturationservice.mapper;


import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.ms.Facturationservice.dtos.LigneAchatRequestDto;
import org.ms.Facturationservice.dtos.LigneAchatResponseDto;
import org.ms.Facturationservice.entities.Fournisseur;
import org.ms.Facturationservice.entities.LigneAchat;
import org.ms.Facturationservice.entities.Produit;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LigneAchatMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurer la stratégie de mapping strict
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapping LigneAchatRequestDto to LigneAchat
        modelMapper.createTypeMap(LigneAchatRequestDto.class, LigneAchat.class)
                .addMappings(m -> m.skip(LigneAchat::setId))
                .addMappings(m -> m.skip(LigneAchat::setFournisseur))
                .addMappings(m -> m.skip(LigneAchat::setProduit))
                //.addMappings(m -> m.skip(LigneAchat::setSocieteId))
                .addMappings(m -> m.map(LigneAchatRequestDto::getProduitId, LigneAchat::setProduit))
                .addMappings(m -> m.map(LigneAchatRequestDto::getFournisseurId, LigneAchat::setFournisseur))
                .setPostConverter(context -> {
                    LigneAchatRequestDto source = context.getSource();
                    LigneAchat destination = context.getDestination();
                    Produit produit = new Produit();
                    produit.setId(source.getProduitId());
                    destination.setProduit(produit);
                    Fournisseur fournisseur = new Fournisseur();
                    fournisseur.setId(source.getFournisseurId());
                    destination.setFournisseur(fournisseur);
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });

        // Mapping LigneAchat to LigneAchatResponseDto
        modelMapper.createTypeMap(LigneAchat.class, LigneAchatResponseDto.class)
                .addMappings(m -> m.map(src -> src.getFournisseur().getId(), LigneAchatResponseDto::setFournisseurId))
                .addMappings(m -> m.map(src -> src.getProduit().getId(), LigneAchatResponseDto::setProduitId))
                .setPostConverter(context -> {
                    LigneAchat source = context.getSource();
                    LigneAchatResponseDto destination = context.getDestination();
                    destination.setFournisseurId(source.getFournisseur().getId());
                    destination.setFournisseurNom(source.getFournisseur().getNom());
                    destination.setProduitId(source.getProduit().getId());
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });
        return modelMapper;
    }
}

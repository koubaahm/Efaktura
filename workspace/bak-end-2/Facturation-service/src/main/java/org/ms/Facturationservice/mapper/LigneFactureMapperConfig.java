
package org.ms.Facturationservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.ms.Facturationservice.dtos.LigneFactureRequestDto;
import org.ms.Facturationservice.dtos.LigneFactureResponseDto;
import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.entities.LigneFacture;
import org.ms.Facturationservice.entities.Produit;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

//@Configuration
@Component
public class LigneFactureMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Configurer la stratégie de mapping strict
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapping LigneFactureRequestDto to LigneFacture
        modelMapper.createTypeMap(LigneFactureRequestDto.class, LigneFacture.class)
                .addMappings(m -> m.skip(LigneFacture::setId))
                .addMappings(m -> m.skip(LigneFacture::setFacture))
                .addMappings(m -> m.skip(LigneFacture::setProduit))
                .addMappings(m -> m.map(LigneFactureRequestDto::getProduitId, LigneFacture::setProduit))
                .addMappings(m -> m.map(LigneFactureRequestDto::getFactureId, LigneFacture::setFacture))
                .setPostConverter(context -> {
                    LigneFactureRequestDto source = context.getSource();
                    LigneFacture destination = context.getDestination();
                    Produit produit = new Produit();
                    produit.setId(source.getProduitId());
                    destination.setProduit(produit);
                    Facture facture = new Facture();
                    facture.setId(source.getFactureId());
                    destination.setFacture(facture);
                    return destination;
                });

        // Mapping LigneFacture to LigneFactureResponseDto
        modelMapper.createTypeMap(LigneFacture.class, LigneFactureResponseDto.class)
                .addMappings(m -> m.map(src -> src.getFacture().getId(), LigneFactureResponseDto::setFactureId))
                .addMappings(m -> m.map(src -> src.getProduit().getId(), LigneFactureResponseDto::setProduitId))
                .setPostConverter(context -> {
                    LigneFacture source = context.getSource();
                    LigneFactureResponseDto destination = context.getDestination();
                    destination.setFactureId(source.getFacture().getId());
                    destination.setProduitId(source.getProduit().getId());
                    return destination;
                });
        return modelMapper;
    }
}

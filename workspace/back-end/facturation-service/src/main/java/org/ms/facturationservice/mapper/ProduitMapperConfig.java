
package org.ms.Facturationservice.mapper;
import org.modelmapper.ModelMapper;
import org.ms.Facturationservice.dto.ProduitRequestDto;
import org.ms.Facturationservice.dto.ProduitResponseDto;
import org.ms.Facturationservice.entities.Produit;
import org.ms.Facturationservice.entities.Tva;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ProduitMapperConfig {



    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de ProduitRequestDto vers Produit
        modelMapper.createTypeMap(ProduitRequestDto.class, Produit.class)
                .addMappings(m -> m.skip(Produit::setId))
                .addMappings(m -> m.skip(Produit::setTva))
                .setPostConverter(context -> {
                    ProduitRequestDto source = context.getSource();
                    Produit destination = context.getDestination();
                    Tva tva = new Tva();
                    tva.setId(source.getTvaId());
                    destination.setTva(tva);
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });

        // Mapping de Produit vers ProduitResponseDto
        modelMapper.createTypeMap(Produit.class, ProduitResponseDto.class)
                .addMappings(m -> m.skip(ProduitResponseDto::setTvaId))
                .setPostConverter(context -> {
                    Produit source = context.getSource();
                    ProduitResponseDto destination = context.getDestination();
                    destination.setTvaId(source.getTva().getId());
                    destination.setSocieteId(destination.getSocieteId());
                    return destination;
                });

        return modelMapper;
    }
}


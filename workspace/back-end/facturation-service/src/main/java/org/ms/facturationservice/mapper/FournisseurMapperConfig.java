package org.ms.Facturationservice.mapper;

import org.modelmapper.ModelMapper;
import org.ms.Facturationservice.dto.FournisseurRequestDto;
import org.ms.Facturationservice.dto.FournisseurResponseDto;
import org.ms.Facturationservice.entities.Fournisseur;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;



@Component
public class FournisseurMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de FournisseurRequestDto vers Fournisseur
        modelMapper.createTypeMap(FournisseurRequestDto.class, Fournisseur.class)
                .addMappings(mapper -> mapper.skip(Fournisseur::setId))
                .addMappings(mapper -> mapper.skip(Fournisseur::setSocieteId))
                .setPostConverter(context -> {
            FournisseurRequestDto source = context.getSource();
            Fournisseur destination = context.getDestination();
            destination.setSocieteId(source.getSocieteId());
            return destination;
        });

        // Mapping de Fournisseur vers FournisseurResponseDto
        modelMapper.createTypeMap(Fournisseur.class, FournisseurResponseDto.class)
                .setPostConverter(context -> {
                    Fournisseur source = context.getSource();
                    FournisseurResponseDto destination =context.getDestination();
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });

        return modelMapper;



    }
}

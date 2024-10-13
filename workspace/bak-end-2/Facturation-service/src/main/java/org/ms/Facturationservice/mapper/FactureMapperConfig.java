package org.ms.Facturationservice.mapper;

import org.modelmapper.ModelMapper;
import org.ms.Facturationservice.dtos.FactureRequestDto;
import org.ms.Facturationservice.dtos.FactureResponseDto;
import org.ms.Facturationservice.entities.Client;
import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.entities.SocieteResponseDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class FactureMapperConfig {


    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de FactureRequestDto vers Facture
        modelMapper.createTypeMap(FactureRequestDto.class, Facture.class)
                .addMappings(m -> m.skip(Facture::setClient))
                .addMappings(m -> m.skip(Facture::setSocieteId))
                .addMappings(m -> m.skip(Facture::setId))
                .setPostConverter(context -> {
                    FactureRequestDto source = context.getSource();
                    Facture destination = context.getDestination();

                    SocieteResponseDTO societe = new SocieteResponseDTO();
                    societe.setId(source.getSocieteId());
                    destination.setSocieteId(societe.getId());
                    Client client = new Client();
                    client.setId(source.getClientId());
                    destination.setClient(client);
                    return destination;
                });

        // Mapping de Facture vers FactureResponseDto
        modelMapper.createTypeMap(Facture.class, FactureResponseDto.class)
                .addMappings(m -> m.skip(FactureResponseDto::setClientId))
                .addMappings(m -> m.skip(FactureResponseDto::setSocieteId))
                .addMappings(m -> m.skip(FactureResponseDto::setId))
                .setPostConverter(context -> {
                    Facture source = context.getSource();
                    FactureResponseDto destination = context.getDestination();
                    destination.setId(source.getId());
                    destination.setClientId(1L);
                    destination.setSocieteId(1L);
                    return destination;
                });

        return modelMapper;
    }


}


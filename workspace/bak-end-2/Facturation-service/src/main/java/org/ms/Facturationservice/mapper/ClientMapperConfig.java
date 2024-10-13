
package org.ms.Facturationservice.mapper;


import org.modelmapper.ModelMapper;
import org.ms.Facturationservice.dtos.ClientRequestDto;
import org.ms.Facturationservice.dtos.ClientResponseDto;
import org.ms.Facturationservice.entities.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ClientMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de ClientRequestDto vers Client
        //ClientRequestDto source = context.getSource();
        modelMapper.createTypeMap(ClientRequestDto.class, Client.class)
                .addMappings(m -> m.skip(Client::setId))
                .addMappings(m -> m.skip(Client::setSocieteId))
                .setPostConverter(context -> {
                    ClientRequestDto source = context.getSource();
                    Client destination = context.getDestination();
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });

        // Mapping de Client vers ClientResponseDto
        modelMapper.createTypeMap(Client.class, ClientResponseDto.class)
                .setPostConverter(context -> {
                    Client source = context.getSource();
                    ClientResponseDto destination =context.getDestination();
                    destination.setSocieteId(source.getSocieteId());
                    return destination;
                });

        return modelMapper;
    }
}




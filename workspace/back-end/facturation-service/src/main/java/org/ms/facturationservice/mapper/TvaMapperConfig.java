package org.ms.Facturationservice.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import org.ms.Facturationservice.dto.TvaRequestDto;
import org.ms.Facturationservice.dto.TvaResponseDto;
import org.ms.Facturationservice.entities.Tva;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class TvaMapperConfig {

@Bean
public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();

    // Mapping de ClientRequestDto vers Client
    //ClientRequestDto source = context.getSource();
    modelMapper.createTypeMap(TvaRequestDto.class, Tva.class)
            .addMappings(m -> m.skip(Tva::setId))
            .setPostConverter(MappingContext::getDestination);


    // Mapping de Client vers ClientResponseDto
    modelMapper.createTypeMap(Tva.class, TvaResponseDto.class)
            .setPostConverter(context -> {
                Tva source = context.getSource();
                return context.getDestination();
            });

    return modelMapper;
}

}

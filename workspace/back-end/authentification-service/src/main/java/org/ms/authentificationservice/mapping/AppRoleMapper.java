package org.ms.authentificationservice.mapping;

import org.modelmapper.ModelMapper;
import org.ms.authentificationservice.dtos.AppRoleRequestDTO;
import org.ms.authentificationservice.dtos.AppRoleResponseDTO;
import org.ms.authentificationservice.entities.AppRole;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppRoleMapper {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de AppRole vers AppRoleResponseDTO
        modelMapper.createTypeMap(AppRole.class, AppRoleResponseDTO.class)
                .setPostConverter(context -> {
                    AppRole source = context.getSource();
                    return context.getDestination();
                });

        // Mapping de AppRole vers AppRoleRequestDTO
        modelMapper.createTypeMap(AppRole.class, AppRoleRequestDTO.class)
                .setPostConverter(context -> {
                    AppRole source = context.getSource();
                    return context.getDestination();
                });

        return modelMapper;
    }
}

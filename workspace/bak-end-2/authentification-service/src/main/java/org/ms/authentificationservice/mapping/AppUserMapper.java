package org.ms.authentificationservice.mapping;

import org.modelmapper.ModelMapper;
import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de AppUserRequestDTO vers AppUser
        modelMapper.createTypeMap(AppUserRequestDTO.class, AppUser.class)
                .addMappings(m -> m.skip(AppUser::setId))
//                .addMappings(m -> m.skip(AppUser::setSociete))
                .setPostConverter(context -> {
                    AppUserRequestDTO source = context.getSource();
                    AppUser destination = context.getDestination();
                    if (source.getSocieteId() != null) {
                        Societe societe = new Societe();
                        societe.setId(source.getSocieteId());
                        destination.setSociete(societe);
                    } else {
                        destination.setSociete(null);
                    }
                    if (source.getMySocieteId() != null) {
                        Societe mySociete = new Societe();
                        mySociete.setId(source.getMySocieteId());
                        destination.setSociete(mySociete);
                    } else {
                        destination.setSociete(null);
                    }
                    return destination;
                });

        // Mapping de AppUser vers AppUserResponseDTO
        modelMapper.createTypeMap(AppUser.class, AppUserResponseDTO.class)
                .addMappings(m -> m.skip(AppUserResponseDTO::setSocieteId))
                .setPostConverter(context -> {
                    AppUser source = context.getSource();
                    AppUserResponseDTO destination = context.getDestination();
                    if (source.getSociete() != null) {
                        destination.setSocieteId(source.getSociete().getId());

                    } else {
                        destination.setSocieteId(null);


                    }
                    if (source.getMySociete() != null) {
                        destination.setMySocieteId(source.getMySociete().getId());

                    } else {
                        destination.setMySocieteId(null);


                    }
                    return destination;
                });

        return modelMapper;
    }
}

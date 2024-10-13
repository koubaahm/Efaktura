package org.ms.authentificationservice.mapping;

import org.modelmapper.ModelMapper;
import org.ms.authentificationservice.dtos.SocieteRequestDTO;
import org.ms.authentificationservice.dtos.SocieteResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


//
@Component
public class SocieteMapper {

    private final ModelMapper modelMapper;

    public SocieteMapper() {
        this.modelMapper = new ModelMapper();
        configureMappings();
    }

    private void configureMappings() {
        modelMapper.createTypeMap(SocieteRequestDTO.class, Societe.class)
                .addMappings(mapper -> mapper.skip(Societe::setId))
                .addMappings(mapper -> mapper.skip(Societe::setAdminUser))
                .addMappings(mapper -> mapper.skip(Societe::setAbonnement))
                .setPostConverter(context -> {
                    SocieteRequestDTO source = context.getSource();
                    Societe destination = context.getDestination();
                    Abonnement abonnement=new Abonnement();
                    abonnement.setId(source.getAbonnementId());
                    destination.setAbonnement(abonnement);
                    if (source.getAdminUserId() != null) {
                        AppUser appUser = new AppUser();
                        appUser.setId(source.getAdminUserId());
                        destination.setAdminUser(appUser);
                    }
                    return destination;
                });

        modelMapper.createTypeMap(Societe.class, SocieteResponseDTO.class)
                .addMappings(mapper -> mapper.skip(SocieteResponseDTO::setAdminUserId))
                .setPostConverter(context -> {
                    Societe source = context.getSource();
                    SocieteResponseDTO destination = context.getDestination();
                    if (source.getAdminUser() != null) {
                        destination.setAdminUserId(source.getAdminUser().getId());
                    }
                    if (source.getAbonnement() != null) {
                        destination.setAbonnementId(source.getAbonnement().getId());
                    }

                    return destination;
                });
    }

    @Bean
    public ModelMapper modelMapper() {
        return modelMapper;
    }
}


package org.ms.authentificationservice.mapping;


import org.modelmapper.ModelMapper;
import org.ms.authentificationservice.dtos.AbonnementRequestDTO;
import org.ms.authentificationservice.dtos.AbonnementResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class AbonnementMapper {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Mapping de AbonnementRequestDto vers Abonnement
        modelMapper.createTypeMap(AbonnementRequestDTO.class, Abonnement.class)
                .addMappings(m -> m.skip(Abonnement::setSociete))
                .addMappings(m -> m.skip(Abonnement::setId))
                .setPostConverter(context -> {
                    AbonnementRequestDTO source = context.getSource();
                    Abonnement destination = context.getDestination();
                    //destination.setDateDebut(LocalDate.now());
//                    Societe societe = new Societe();
//                    societe.setId(source.getSocieteId());
//                    destination.setSociete(societe);
                    return destination;
                });

        // Mapping de Abonnement vers AbonnementResponseDto
        modelMapper.createTypeMap(Abonnement.class, AbonnementResponseDTO.class)
                .addMappings(m -> m.skip(AbonnementResponseDTO::setId))
                .setPostConverter(context -> {
                    Abonnement source = context.getSource();
                    AbonnementResponseDTO destination = context.getDestination();
                    destination.setId(source.getId());
                    destination.setActive(source.getActive());
                    if (source.getSociete() != null) {
                        destination.setSocieteId(source.getSociete().getId());
                    }

                    return destination;
                });


        return modelMapper;
    }
}

package org.ms.authentificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponseDTO {

    private Long id;
    private String nom;
    private String email;
    private String password;
    private String adresse;
    private String telephone;
    private Set<Long> rolesIds;
    private Set<String> rolesNom;
    private Long societeId;
    private Long mySocieteId;
    private Long abonnementId;
}

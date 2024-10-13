package org.ms.authentificationservice.dtos;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocieteResponseDTO {
    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String matriculeFiscal;
    private Long abonnementId;
    private Long adminUserId;
    private List<Long> utilisateurIds;
}

package org.ms.Facturationservice.entities;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SocieteResponseDTO {

    private Long id;
    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String matriculeFiscal;
}

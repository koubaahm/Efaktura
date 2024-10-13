package org.ms.Facturationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SocieteRequestDTO {
    @NotBlank
    private String nom;

    @NotBlank
    private String email;

    @NotBlank
    private String telephone;

    @NotBlank
    private String adresse;

    @NotBlank
    private String matriculeFiscal;
    @NotBlank
    private Long adminUserId;

    @NotNull
   private Long abonnementId;

//    @NotNull(message = "La liste des utilisateurs ne doit pas être null")
//    private List<AppUserRequestDTO> appUserRequestDTOList;
}

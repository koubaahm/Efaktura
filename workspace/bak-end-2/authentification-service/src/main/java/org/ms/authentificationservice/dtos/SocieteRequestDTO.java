package org.ms.authentificationservice.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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

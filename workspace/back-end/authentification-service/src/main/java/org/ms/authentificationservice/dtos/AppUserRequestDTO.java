package org.ms.authentificationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequestDTO {
    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom doit avoir au maximum 100 caractères")
    private String nom;

    @NotBlank(message = "L'email est requis")
    @Size(max = 100, message = "L'email doit avoir au maximum 100 caractères")
    @Email(message = "L'email doit être une adresse email valide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit avoir au moins 8 caractères")
    private String password;

    @NotBlank(message = "L'adresse est requise")
    private String adresse;

    @NotBlank(message = "Le numéro de téléphone est requis")
    @Size(max = 20, message = "Le numéro de téléphone doit avoir au maximum 20 caractères")
    private String telephone;

    private Long societeId;
    private Long mySocieteId;
}

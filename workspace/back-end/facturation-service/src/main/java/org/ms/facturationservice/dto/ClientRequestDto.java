package org.ms.Facturationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data@AllArgsConstructor@NoArgsConstructor@Builder
public class ClientRequestDto {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas dépasser {max} caractères")
    private String nom;

    @NotBlank(message = "L'adresse e-mail est obligatoire")
    @Email(message = "L'adresse e-mail n'est pas valide")
    @Size(max = 50, message = "L'adresse e-mail ne doit pas dépasser {max} caractères")
    private String email;

    @NotNull(message = "Le numéro de téléphone est obligatoire")
    @Digits(integer = 8, fraction = 0, message = "Le numéro de téléphone doit contenir exactement {integer} chiffres")
    private Long telephone;
    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 150, message = "L'adresse' ne doit pas dépasser {max} caractères")
    private String adresse;
    @NotBlank(message = "Le cin_MatriculeFiscal est obligatoire")
    @Size(max = 50, message = "Le cin_MatriculeFiscal ne doit pas dépasser {max} caractères")
    private String cin_MatriculeFiscal;
    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;
}

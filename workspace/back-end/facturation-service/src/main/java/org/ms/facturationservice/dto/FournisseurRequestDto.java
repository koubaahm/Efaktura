package org.ms.Facturationservice.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Validated
public class FournisseurRequestDto {

    private Long id;

    @NotBlank(message = "Le nom ne doit pas être vide")
    private String nom;
    @NotBlank(message = "L'adresse ne doit pas être vide")
    private String adresse;
    @NotBlank(message = "Le numéro de téléphone ne doit pas être vide")
    private String telephone;
    @NotNull(message = "La liste des lignes d'achats ne doit pas être null")
    private List<LigneAchatRequestDto> ligneAchats;
    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;
}

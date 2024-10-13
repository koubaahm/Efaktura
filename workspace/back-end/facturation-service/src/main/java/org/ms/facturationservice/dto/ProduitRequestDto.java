package org.ms.Facturationservice.dto;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Validated
public class ProduitRequestDto {

    private Long id;

    @NotBlank(message = "Le label ne doit pas être vide")
    private String label;
    @NotBlank(message = "La description ne doit pas être vide")
    private String description;
    @NotNull(message = "La quantité doit être renseignée")
    @Positive(message = "La quantité doit être positive")
    private double quantite;
    @NotNull(message = "La marge doit être renseignée")
    @Positive(message = "La marge doit être positive")
    private double marge;
    @NotNull(message = "L'identifiant du stock doit être renseigné")
    private Long tvaId;
    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;

}

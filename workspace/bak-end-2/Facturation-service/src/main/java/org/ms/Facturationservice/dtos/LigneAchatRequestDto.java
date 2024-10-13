package org.ms.Facturationservice.dtos;

import lombok.*;
import org.springframework.validation.annotation.Validated;

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
public class LigneAchatRequestDto {

    private Long id;

    @NotNull(message = "L'identifiant du produit doit être renseigné")
    private Long produitId;
    @NotNull(message = "L'identifiant du fournisseur doit être renseigné")
    private Long fournisseurId;
    @NotNull(message = "La quantité doit être renseignée")
    @Positive(message = "La quantité doit être positive")
    private int quantiteAchat;
    @NotNull(message = "Le prix unitaire doit être renseigné")
    @Positive(message = "Le prix unitaire doit être positif")
    private double prixUnitaire;
    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;

}

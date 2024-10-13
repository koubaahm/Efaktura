
package org.ms.Facturationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneFactureRequestDto {

    Long id;

    @NotNull(message = "La quantité ne peut pas être nulle")
    @Min(value = 1, message = "La quantité doit être supérieure ou égale à 1")
    private Integer quantite;

    @NotNull(message = "Le prix unitaire ne peut pas être nul")
    @Min(value = 0, message = "Le prix unitaire doit être supérieur ou égal à 0")
    private Double prixUnitaire;

    @NotNull(message = "Le taux remise ne peut pas être nul")
    @Min(value = 0, message = "Le taux remise doit être supérieur ou égal à 0")
    private int tauxRemise;

    @NotNull(message = "Le produit ne peut pas être nul")
    private Long produitId;

    @NotNull(message = "La facture ne peut pas être nulle")
    private Long factureId;
    @NotNull(message = "La ligne Achat ne peut pas être nulle")
    private Long idLigneAchat;

}

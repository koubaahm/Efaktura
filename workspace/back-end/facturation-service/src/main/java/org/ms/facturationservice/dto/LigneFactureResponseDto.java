package org.ms.Facturationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LigneFactureResponseDto {

    private Long id;
    private int quantite;
    private double prixUnitaire;
    private int tauxRemise;
    private double remise;
    private double prixVente;
    private Long factureId;
    private Long produitId;
    private double total;


}



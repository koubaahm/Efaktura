package org.ms.Facturationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LigneAchatResponseDto {

    private Long id;
    private LocalDate date;
    private Long produitId;
    private Long fournisseurId;
    private String fournisseurNom;
    private int quantiteAchat;
    private int quantiteStock;
    private double prixUnitaire;
    private double prixTotal;
    private Long societeId;

}


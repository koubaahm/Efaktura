package org.ms.Facturationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FactureResponseDto {
    private Long id;
    private Long societeId;
    private String numeroFacture;
    private LocalDate date;
    private String modePaiement;
    private String statut;
    private String commentaires;
    private double timbreFiscale;
    private double totalHT;
    private double totalTva;
    private double totalRemises;
    private double totalTTC;
    private double totalAPayer;
    private Long clientId;
    private List<Long> factureLigneIds;
}

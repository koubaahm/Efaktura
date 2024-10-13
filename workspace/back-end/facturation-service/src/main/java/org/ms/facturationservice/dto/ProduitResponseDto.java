package org.ms.Facturationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduitResponseDto {

    private Long id;
    private String label;
    private String description;
    private double quantite;
    private double marge;
    private Long tvaId;
    private Long societeId;

}

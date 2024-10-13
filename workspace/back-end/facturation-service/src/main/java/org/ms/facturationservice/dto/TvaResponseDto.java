package org.ms.Facturationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TvaResponseDto {

    private Long id;
    private String label;
    private double valeur;
    private List<Long> produitIds;
    private Long societeId;

}


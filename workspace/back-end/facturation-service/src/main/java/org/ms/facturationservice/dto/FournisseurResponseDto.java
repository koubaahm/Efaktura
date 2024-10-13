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
public class FournisseurResponseDto {

    private Long id;
    private String nom;
    private String adresse;
    private String telephone;
    private Long societeId;
    private List<Long> ligneAchatIds;

}


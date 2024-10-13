package org.ms.Facturationservice.dtos;

import lombok.*;

import java.util.List;
@Data@AllArgsConstructor@NoArgsConstructor@ToString@Builder@Getter@Setter
public class ClientResponseDto {

    private Long id;
    private String nom;
    private String email;
    private Long telephone;
    private String adresse;
    private String cin_MatriculeFiscal;
    private Long societeId;
    private List<Long> factureIds;


}

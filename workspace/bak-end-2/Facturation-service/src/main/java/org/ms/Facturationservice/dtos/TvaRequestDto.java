package org.ms.Facturationservice.dtos;

import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Validated
public class TvaRequestDto {

    private Long id;
    private String label;
    private double valeur;
    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;
}


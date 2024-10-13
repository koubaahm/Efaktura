package org.ms.Facturationservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class FactureRequestDto {



    private Long id;


    @NotBlank(message = "Le mode de paiement ne doit pas être vide")
    private String modePaiement;

    @NotBlank(message = "La date ne doit pas être vide")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @NotBlank(message = "Le statut de la facture ne doit pas être vide")
    private String statut;

    @Size(max = 255, message = "Les commentaires ne doivent pas dépasser 255 caractères")
    private String commentaires;

    @NotNull(message = "L'ID du client ne doit pas être null")
    private Long clientId;

    @NotNull(message = "L'ID de la societe ne doit pas être null")
    private Long societeId;




    private List<LigneFactureRequestDto> factureLignes = new ArrayList<>();
}

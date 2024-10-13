package org.ms.authentificationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "abonnement")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Abonnement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    private LocalDate dateDebut =LocalDate.now();
//    private LocalDate dateFin =dateDebut.plusYears(1);

    LocalDateTime dateDebut = LocalDateTime.now();
    LocalDateTime dateFin = dateDebut.plus(2, ChronoUnit.MINUTES);

    private Boolean active;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "societe_id")
    private Societe societe;

    public LocalDateTime obtenirDateFinAbonnement() {
        LocalDateTime dateDebut = LocalDateTime.now();
        LocalDateTime dateFin = dateDebut.plus(1, ChronoUnit.MINUTES);
        return dateFin;
    }


}

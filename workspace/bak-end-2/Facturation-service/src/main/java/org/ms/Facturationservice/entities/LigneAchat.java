package org.ms.Facturationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LigneAchat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date=LocalDate.now();
    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;
    @ManyToOne
    @JoinColumn(name = "fournisseur_id", nullable = false)
    private Fournisseur fournisseur;
    private int quantiteAchat;
    private double prixUnitaire;
    private int quantiteStock;

    @Column(name = "societe_Id")
    private Long societeId;

    public double getPrixTotal() {
        return prixUnitaire * quantiteAchat;
    }
}


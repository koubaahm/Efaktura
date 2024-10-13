package org.ms.Facturationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantite;

    private double prixUnitaire;

    @ManyToOne
    private Facture facture;

    @ManyToOne
    private Produit produit;

    private int tauxRemise ;

    private Long idLigneAchat;



    public double getRemise() {
         return (prixUnitaire * tauxRemise) / 100;
    }

    public double getPrixVente() {
        return prixUnitaire -getRemise();
    }

    public double getTotal() {
        return quantite * prixUnitaire;
    }

    public double getTotalTva() {
        return prixUnitaire * produit.getTva().getValeur();
    }
}




package org.ms.Facturationservice.entities;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Facture {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "date_facture")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private String date;

    @Column(name = "mode_paiement_facture")
    private String modePaiement;

    @Column(name = "statut_facture")
    private String statut;

    @Column(name = "commentaires_facture")
    private String commentaires;

    @Column(name = "timbre_fiscale")
    private double timbreFiscale =1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "facture",cascade = CascadeType.ALL)
    private List<LigneFacture> ligneFactures;

    @Column(name = "numero_facture")
    private String numeroFacture;

    @Column(name = "societe_Id")
    private Long societeId;

    private static int counter = 1; // Compteur global de factures
    private static String lastGeneratedNumeroFacture; // Attribut statique pour stocker le dernier numéro de facture généré

    public Facture(String date,  String modePaiement, String statut, String commentaires, List<LigneFacture> factureLignes, Client client,String numeroFacture) {
        this.date = date;
        this.modePaiement = modePaiement;
        this.statut = statut;
        this.commentaires = commentaires;
        this.ligneFactures = factureLignes;
        this.client = client;
        this.numeroFacture=numeroFacture;
    }

    public double getTotalHT() {
        return ligneFactures.stream()
                .mapToDouble(LigneFacture::getTotal)
                .sum();
    }
    public double getTotalTva() {
        return ligneFactures.stream()
                .mapToDouble(LigneFacture::getTotalTva)
                .sum();
    }

    public double getTotalRemises() {
        return ligneFactures.stream()
                .mapToDouble(LigneFacture::getRemise)
                .sum();
    }

    public double getTotalTTC() {
        return getTotalHT() +getTotalTva();
    }

    public double getTotalAPayer() {
        return getTotalTTC()+timbreFiscale - getTotalRemises();
    }

    public void addFactureLigne(LigneFacture factureLigne) {
        factureLigne.setFacture(this);
        this.ligneFactures.add(factureLigne); // Mettre à jour la relation inverse
    }








}

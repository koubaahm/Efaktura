package org.ms.Facturationservice.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Produit {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String label;
    @Column(nullable = false)
    private String description;


    @Column(nullable = false)
    private double quantite;


    private double marge;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "tva_id")
    private Tva tva;

    @Column(name = "societe_Id")
    private Long societeId;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<LigneAchat> ligneAchats;



    @OneToMany(mappedBy = "produit", cascade = CascadeType.REMOVE)
    private List<LigneFacture> ligneFactures;
}









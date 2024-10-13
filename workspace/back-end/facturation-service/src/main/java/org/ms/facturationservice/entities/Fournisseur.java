package org.ms.Facturationservice.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
public class Fournisseur {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String adresse;
    @Column(nullable = false)
    private String telephone;
    @Column(name = "societe_Id")
    private Long societeId;
    @OneToMany(mappedBy = "fournisseur",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneAchat> ligneAchats;
}


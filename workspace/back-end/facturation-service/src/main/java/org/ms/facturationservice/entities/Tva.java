package org.ms.Facturationservice.entities;


import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Data@AllArgsConstructor@NoArgsConstructor@ToString@Builder
public class Tva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String label;
    private double valeur;
    //@JsonManagedReference
    @Column(name = "societe_Id")
    private Long societeId;
    @OneToMany(mappedBy = "tva",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Produit> produits;


}

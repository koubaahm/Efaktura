package org.ms.Facturationservice.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Data@NoArgsConstructor@AllArgsConstructor@ToString@Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String email;
    private Long telephone;
    private String adresse;
    private String cin_MatriculeFiscal;

    @Column(name = "societe_Id")
    private Long societeId;

    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL, fetch= FetchType.LAZY)
    private List<Facture> factures;

}

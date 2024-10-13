package org.ms.authentificationservice.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "societe")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Societe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;
    private String telephone;
    private String adresse;
    private String matriculeFiscal;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "abonnement_id")
    private Abonnement abonnement;

    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name ="adminUser_id")
    private AppUser adminUser;

    @OneToMany(mappedBy = "societe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppUser> operateurs = new ArrayList<>();
}

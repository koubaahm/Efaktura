package org.ms.authentificationservice.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "utilisateur")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom doit avoir au maximum 100 caractères")
    private String nom;

    @NotBlank(message = "L'email est requis")
    @Size(max = 100, message = "L'email doit avoir au maximum 100 caractères")
    @Email(message = "L'email doit être une adresse email valide")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 6, message = "Le mot de passe doit avoir au moins 6 caractères")
    private String password;

    private String adresse;

    @Size(max = 20, message = "Le numéro de téléphone doit avoir au maximum 20 caractères")
    @Column(unique = true)
    private String telephone;

    private LocalDate dateAjout = LocalDate.now();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "societe_id")
    private Societe societe;

    @OneToOne( mappedBy = "adminUser")
    private Societe mySociete;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<AppRole> roles = new HashSet<>();

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, password);
    }

    public Societe getAdminSociete() {
        if (hasAdminRole()) {
            return mySociete;
        }
        return null;
    }

    private boolean hasAdminRole() {
        return roles.stream().anyMatch(role -> role.getRolename().equals("Admin"));
    }
}

package org.ms.authentificationservice.repositories;

import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocieteRepository extends JpaRepository<Societe,Long> {

    Societe findByAbonnement(Abonnement abonnement);
}

package org.ms.Facturationservice.repositories;



import org.ms.Facturationservice.entities.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur,Long> {

    List<Fournisseur> findBySocieteId(Long societeId);
}

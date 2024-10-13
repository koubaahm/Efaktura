package org.ms.Facturationservice.repositories;

import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.entities.LigneFacture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LigneFactureRepository extends JpaRepository<LigneFacture,Long> {

    List<LigneFacture> findByFacture(Facture facture);

}

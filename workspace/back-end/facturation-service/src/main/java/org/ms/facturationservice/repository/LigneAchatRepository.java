package org.ms.Facturationservice.repository;



import org.ms.Facturationservice.entities.LigneAchat;
import org.ms.Facturationservice.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface LigneAchatRepository extends JpaRepository<LigneAchat,Long> {

    List<LigneAchat> findByProduit(Produit produit);
    List<LigneAchat> findBySocieteId(Long societeId);
    LigneAchat  findByProduitAndPrixUnitaire(Produit produit, double PrixUnitaire);

}

package org.ms.Facturationservice.repository;



import org.ms.Facturationservice.entities.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture,Long> {

    List<Facture> findBySocieteId(Long societeId);


//    @Query("SELECT MAX(f.numeroFacture) FROM Facture f WHERE f.societeId = :societeId")
//    String findLastNumeroFactureBySocieteId(@Param("societeId") Long societeId);

    @Query("SELECT f FROM Facture f WHERE f.societeId = :societeId AND f.numeroFacture = (SELECT MAX(f2.numeroFacture) FROM Facture f2 WHERE f2.societeId = :societeId)")
    Facture findLastFactureBySocieteId(@Param("societeId") Long societeId);



}

package org.ms.Facturationservice.repository;




import org.ms.Facturationservice.entities.Tva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvaRepository extends JpaRepository<Tva,Long> {

    List<Tva> findBySocieteId(Long societeId);

}

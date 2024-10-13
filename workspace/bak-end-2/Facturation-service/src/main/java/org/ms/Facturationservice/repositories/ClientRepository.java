package org.ms.Facturationservice.repositories;



import org.ms.Facturationservice.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client,Long> {

    List<Client> findBySocieteId(Long societeId);
}

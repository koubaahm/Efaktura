package org.ms.authentificationservice.repositories;


import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    AppUser findByNom(String username);

    AppUser findByEmail(String email);

    List<AppUser> findAllBySociete(Societe societe);
}

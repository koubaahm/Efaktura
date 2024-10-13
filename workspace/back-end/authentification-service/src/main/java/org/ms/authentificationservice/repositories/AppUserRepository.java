package org.ms.authentificationservice.repositories;


import org.ms.authentificationservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AppUserRepository extends JpaRepository<AppUser,Long> {

    AppUser findByNom(String username);
}

package org.ms.authentificationservice.repositories;

import org.ms.authentificationservice.entities.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole,Long> {

    AppRole findByrolename(String userRole);
}

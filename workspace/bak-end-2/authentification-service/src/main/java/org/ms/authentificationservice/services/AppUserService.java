package org.ms.authentificationservice.services;


import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AppUserService {
    List<AppUserResponseDTO> list();

    List<AppUserResponseDTO> listBySocieteId(Long societeId);

    AppUserResponseDTO getOne(Long id);

    Abonnement getAbonnementByAdminUser(Long adminUserId);

    ResponseEntity<AppUserResponseDTO> save(AppUserRequestDTO appUserRequestDTO);
    ResponseEntity<AppUserResponseDTO> saveAdmin (AppUserRequestDTO appUserRequestDTO);

    ResponseEntity<AppUserResponseDTO> saveOperateur(AppUserRequestDTO appUserRequestDTO);

    ResponseEntity<AppUserResponseDTO> saveSuperAdmin(AppUserRequestDTO appUserRequestDTO);

    ResponseEntity<AppUserResponseDTO> update(@PathVariable Long id, AppUserRequestDTO appUserRequestDTO);

    ResponseEntity<?> delete(Long id);

    //public AppUserResponseDTO addRoleToUser(String username, String roleName);
    void addRoleToUser(String username, String roleName);

    AppUser getUserByName(String username);

    AppUserResponseDTO  getUserByMail(String mail);
}

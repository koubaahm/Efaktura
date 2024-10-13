package org.ms.authentificationservice.services;


import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AppUserService {
    public List<AppUserResponseDTO> list();

    public AppUserResponseDTO getOne(Long id);

    public ResponseEntity<AppUserResponseDTO> save(AppUserRequestDTO appUserRequestDTO);
    public ResponseEntity<AppUserResponseDTO> saveAdmin(AppUserRequestDTO appUserRequestDTO);

    public ResponseEntity<AppUserResponseDTO> update(@PathVariable Long id, AppUserRequestDTO appUserRequestDTO);

    public ResponseEntity<?> delete(Long id);

    //public AppUserResponseDTO addRoleToUser(String username, String roleName);
    public void addRoleToUser(String username, String roleName);

    public AppUser getUserByName(String username);
}

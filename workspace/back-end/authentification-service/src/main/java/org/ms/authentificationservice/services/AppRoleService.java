package org.ms.authentificationservice.services;


import org.ms.authentificationservice.dtos.AppRoleRequestDTO;
import org.ms.authentificationservice.dtos.AppRoleResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AppRoleService {
    public List<AppRoleResponseDTO> list();

    public AppRoleResponseDTO getOne(Long id);

    public ResponseEntity<AppRoleResponseDTO> save(AppRoleRequestDTO appRoleRequestDTO);

    public ResponseEntity<AppRoleResponseDTO> update(@PathVariable Long id, AppRoleRequestDTO appRoleRequestDTO);

    public ResponseEntity<?> delete(Long id);
}

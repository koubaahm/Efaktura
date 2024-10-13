package org.ms.authentificationservice.controllers;


import org.ms.authentificationservice.dtos.AppRoleRequestDTO;
import org.ms.authentificationservice.dtos.AppRoleResponseDTO;
import org.ms.authentificationservice.services.AppRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class AppRoleRestController {

    private final AppRoleService appRoleService;


    public AppRoleRestController(AppRoleService appRoleService) {
        this.appRoleService = appRoleService;

    }
    @GetMapping(path = "/roles")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public List<AppRoleResponseDTO> list() {
        return appRoleService.list();
    }


    @GetMapping(path = "/roles/{id}")
    //@PreAuthorize("hasAuthority('ADMIN')")
    public AppRoleResponseDTO getOne(@PathVariable Long id) {
        return appRoleService.getOne(id);

    }


    @PostMapping(path = "/roles")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppRoleResponseDTO> save(@RequestBody AppRoleRequestDTO appRoleRequestDTO) {
        return appRoleService.save(appRoleRequestDTO);

    }


    @PutMapping(path = "/roles/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppRoleResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppRoleRequestDTO appRoleRequestDTO) {
        return appRoleService.update(id,appRoleRequestDTO);

    }


    @DeleteMapping(path = "/roles/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return appRoleService.delete(id);

    }

}

package org.ms.authentificationservice.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.services.AppUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.List;
@RestController
public class AppUserRestController {
    private final AppUserService appUserService;


    public AppUserRestController(AppUserService appUserService) {
        this.appUserService = appUserService;

    }
    @GetMapping(path = "/users")
    //@PreAuthorize("hasAuthority('ADMIN') || hasAuthority('SUPER_ADMIN')")
    public List<AppUserResponseDTO> list() {
        return appUserService.list();
    }
    @GetMapping(path = "/users/bySociete/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppUserResponseDTO> listBySocieteId(@PathVariable Long id){
        return  appUserService.listBySocieteId(id);

    }
    @GetMapping(path = "/users/{id}")
    // @PreAuthorize("hasAuthority('SUPER ADMIN')")
    public AppUserResponseDTO getOne(@PathVariable Long id) {
        return appUserService.getOne(id);

    }
    @GetMapping(path = "/users/byAbonnement/{id}")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public Abonnement getAbonnementByAdminUser(@PathVariable Long id){
        return appUserService.getAbonnementByAdminUser(id);
    }


    @PostMapping(path = "/users")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserResponseDTO> save(@RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.save(appUserRequestDTO);

    }
    @PostMapping(path = "/users/saveAdmin")
    public ResponseEntity<AppUserResponseDTO>saveAdmin(@RequestBody AppUserRequestDTO appUserRequestDTO) {
        System.out.println("99999999999 "+appUserRequestDTO);
        return appUserService.saveAdmin(appUserRequestDTO);

    }
    @PostMapping(path = "/users/saveOperateur")
    public ResponseEntity<AppUserResponseDTO>saveOperateur(@RequestBody AppUserRequestDTO appUserRequestDTO) {
        System.out.println("888888 "+appUserRequestDTO);
        return appUserService.saveOperateur(appUserRequestDTO);
    }
    @PostMapping(path = "/users/saveSuperAdmin")
    public ResponseEntity<AppUserResponseDTO>saveSuperAdmin(@RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.saveSuperAdmin(appUserRequestDTO);
    }


    @PutMapping(path = "/users/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.update(id,appUserRequestDTO);

    }


    @DeleteMapping(path = "/users/{id}")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return appUserService.delete(id);

    }
    @PostMapping(path = "/users/addrole")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public void addRoleToUser(@RequestBody String username, @RequestBody String roleName){
        appUserService.addRoleToUser(username,roleName);

    }
    @GetMapping(path = "/users/byName")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public AppUser getUserByName(@RequestBody String username){
         return appUserService.getUserByName(username);

    }
    @GetMapping(path = "/users/byMail")
    //@PostAuthorize("hasAuthority('ADMIN')")
    public AppUserResponseDTO getUserByMail(@RequestBody String mail){
        return appUserService.getUserByMail(mail);

    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserRoleData{
    private String userName;
    private String roleName;
}
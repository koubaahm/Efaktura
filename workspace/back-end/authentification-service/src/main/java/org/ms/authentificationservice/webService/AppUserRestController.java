package org.ms.authentificationservice.webService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
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
    public List<AppUserResponseDTO> list() {
        return appUserService.list();
    }


    @GetMapping(path = "/users/{id}")
    public AppUserResponseDTO getOne(@PathVariable Long id) {
        return appUserService.getOne(id);

    }


    @PostMapping(path = "/users")
    public ResponseEntity<AppUserResponseDTO> save(@RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.save(appUserRequestDTO);

    }


    @PutMapping(path = "/users/{id}")
    public ResponseEntity<AppUserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody AppUserRequestDTO appUserRequestDTO) {
        return appUserService.update(id,appUserRequestDTO);

    }


    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return appUserService.delete(id);

    }
    @PostMapping(path = "/users/addrole")
    public void addRoleToUser(String username, String roleName){
        appUserService.addRoleToUser(username,roleName);

    }
    @GetMapping(path = "/users/username")
    public AppUser getUserByName(String username){
         return appUserService.getUserByName(username);

    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class UserRoleData{
    private String userName;
    private String roleName;
}
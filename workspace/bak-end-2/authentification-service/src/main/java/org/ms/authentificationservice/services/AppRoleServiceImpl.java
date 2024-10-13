package org.ms.authentificationservice.services;

import org.ms.authentificationservice.dtos.AppRoleRequestDTO;
import org.ms.authentificationservice.dtos.AppRoleResponseDTO;
import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.mapping.AppRoleMapper;
import org.ms.authentificationservice.repositories.AppRoleRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppRoleServiceImpl implements AppRoleService{

    private final AppRoleRepository appRoleRepository;
    private final AppRoleMapper appRoleMapper;

    private final AppUserRepository appUserRepository;

    public AppRoleServiceImpl(AppRoleRepository appRoleRepository,AppRoleMapper appRoleMapper,AppUserRepository appUserRepository){
        this.appRoleRepository=appRoleRepository;
        this.appRoleMapper=appRoleMapper;
        this.appUserRepository=appUserRepository;
    }
    @Override
    public List<AppRoleResponseDTO> list() {
        List<AppRole> appRoles = appRoleRepository.findAll();
        List<AppRoleResponseDTO> appRoleResponseDTOS = new ArrayList<>();

        for (AppRole appRole : appRoles) {
            AppRoleResponseDTO  appRoleResponseDTO = appRoleMapper.modelMapper().map(appRole, AppRoleResponseDTO.class);
            appRoleResponseDTOS.add(appRoleResponseDTO);
        }
        return appRoleResponseDTOS;
    }

    @Override
    public AppRoleResponseDTO getOne(Long id) {
        Optional<AppRole> optionalAppRole = appRoleRepository.findById(id);
        AppRole appRole = optionalAppRole.orElseThrow(() -> new EntityNotFoundException("Role not found"));


        return appRoleMapper.modelMapper().map(appRole, AppRoleResponseDTO.class);
    }

    @Override
    public ResponseEntity<AppRoleResponseDTO> save(AppRoleRequestDTO appRoleRequestDTO) {
        AppRole appRole = appRoleMapper.modelMapper().map(appRoleRequestDTO, AppRole.class);


        appRoleRepository.save(appRole);

        AppRoleResponseDTO appRoleResponseDTO = appRoleMapper.modelMapper().map(appRole, AppRoleResponseDTO.class);


        return new ResponseEntity<>(appRoleResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AppRoleResponseDTO> update(Long id, AppRoleRequestDTO appRoleRequestDTO) {
        Optional<AppRole> appRoleToUpdate = appRoleRepository.findById(id);


        if (appRoleToUpdate.isPresent()) {


            AppRole appRole = appRoleToUpdate.get();
            appRole.setRolename(appRoleRequestDTO.getRolename());


            appRoleRepository.save(appRole);
            AppRoleResponseDTO updatedAppRoleResponseDTO = appRoleMapper.modelMapper().map(appRole, AppRoleResponseDTO.class);
            return new ResponseEntity<>(updatedAppRoleResponseDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        AppRole appRole = appRoleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id " + id));
        List<AppUser> appUsers =appUserRepository.findAll();
        for (AppUser user : appUsers) {
            user.getRoles().remove(appRole);
        }

        appRoleRepository.delete(appRole);
        return new ResponseEntity<>("Rôle supprimé avec succès.", HttpStatus.OK);
    }


}

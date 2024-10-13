package org.ms.authentificationservice.services;

import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.AppRole;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.entities.Societe;
import org.ms.authentificationservice.mapping.AppUserMapper;
import org.ms.authentificationservice.repositories.AppRoleRepository;
import org.ms.authentificationservice.repositories.AppUserRepository;
import org.ms.authentificationservice.repositories.SocieteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService{

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    private  final AppRoleRepository appRoleRepository;

    private final SocieteRepository societeRepository;

    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository,AppUserMapper appUserMapper,SocieteRepository societeRepository,AppRoleRepository appRoleRepository,PasswordEncoder passwordEncoder){

        this.appUserRepository = appUserRepository;
        this.appUserMapper =appUserMapper;
        this.societeRepository=societeRepository;
        this.appRoleRepository=appRoleRepository;
        this.passwordEncoder=passwordEncoder;

    }
    @Override
    public List<AppUserResponseDTO> list() {
        List<AppUser> appUsers = appUserRepository.findAll();
        List<AppUserResponseDTO> appUserResponseDTOS = new ArrayList<>();

        for (AppUser appUser : appUsers) {
            AppUserResponseDTO appUserResponseDTO =appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
            appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
            appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
            if (appUser.getSociete() != null) {
                appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
            }
            if (appUser.getMySociete() != null) {
                appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
            }


//            if (appUser.getSociete() != null) {
//                appUserResponseDTO.setSocieteNom(appUser.getSociete().getNom());
//            }
            appUserResponseDTOS.add(appUserResponseDTO);
        }
        return appUserResponseDTOS;
    }

    @Override
    public AppUserResponseDTO getOne(Long id) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
        AppUser appUser = optionalAppUser.orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        AppUserResponseDTO appUserResponseDTO =appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));



        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }

        return appUserResponseDTO;
    }
    @Override
    public ResponseEntity<AppUserResponseDTO> save(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        if (appUserRequestDTO.getSocieteId() != null) {
            Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);

            appUser.setSociete(societe);
            System.out.println("-------------------------"+appUser.getSociete().getNom());
            assert societe != null;
            societe.getOperateurs().add(appUser);
        } else {
            appUser.setSociete(null);
        }
        if (appUserRequestDTO.getMySocieteId() != null) {
            Societe mySociete = societeRepository.findById(appUserRequestDTO.getMySocieteId()).orElse(null);
            appUser.setMySociete(mySociete);
        } else {
            appUser.setMySociete(null);
        }

        Set<AppRole> appRoles = appUser.getRoles().stream()
                .map(appRoleRequestDTO -> appUserMapper.modelMapper().map(appRoleRequestDTO, AppRole.class))
                .collect(Collectors.toSet());

        appUser.setRoles(appRoles);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // Crypter le mot de passe
        appUserRepository.save(appUser);

        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }

        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<AppUserResponseDTO> saveAdmin(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        if (appUserRequestDTO.getSocieteId() != null) {
            Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);
            appUser.setSociete(societe);
        } else {
            appUser.setSociete(null);
        }
        if (appUserRequestDTO.getMySocieteId() != null) {
            Societe mySociete = societeRepository.findById(appUserRequestDTO.getMySocieteId()).orElse(null);
            appUser.setSociete(mySociete);
        } else {
            appUser.setSociete(null);
        }

        Set<AppRole> appRoles = appUser.getRoles().stream()
                .map(appRoleRequestDTO -> appUserMapper.modelMapper().map(appRoleRequestDTO, AppRole.class))
                .collect(Collectors.toSet());

        appUser.setRoles(appRoles);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // Crypter le mot de passe
        addRoleToUser(appUser.getNom(),"Admin");
        appUserRepository.save(appUser);

        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);

        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }

        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }




    @Override
    public ResponseEntity<AppUserResponseDTO> update(Long id, AppUserRequestDTO appUserRequestDTO) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);

        if (optionalAppUser.isPresent()) {
            AppUser appUserToUpdate = optionalAppUser.get();
            appUserToUpdate.setNom(appUserRequestDTO.getNom());
            appUserToUpdate.setPassword(appUserRequestDTO.getPassword());
            appUserToUpdate.setPassword(passwordEncoder.encode( appUserToUpdate.getPassword()));// crypter le password

            Set<AppRole> appRoles = appUserToUpdate.getRoles().stream()
                    .map(appRoleRequestDTO -> appUserMapper.modelMapper().map(appRoleRequestDTO, AppRole.class))
                    .collect(Collectors.toSet());
            appUserToUpdate.setRoles(appRoles);

            if (appRoles.stream().anyMatch(appRole -> appRole.getRolename().equals("SuperAdmin"))) {
                // L'utilisateur a le rôle "SuperAdmin", donc ne pas associer à une société
                appUserToUpdate.setSociete(null);
            } else {
                // Récupérer et associer la société correspondante
                Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);
                appUserToUpdate.setSociete(societe);
            }

            AppUser updatedAppUser = appUserRepository.save(appUserToUpdate);
            AppUserResponseDTO updatedAppUserResponseDTO = appUserMapper.modelMapper().map(updatedAppUser, AppUserResponseDTO.class);
            return new ResponseEntity<>(updatedAppUserResponseDTO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Override
    public ResponseEntity<?> delete(Long id) {
        AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'identifiant " + id));

        if (appUser != null) {
            // Dissocier l'utilisateur de son rôle associé
            appUser.setRoles(null);
            appUserRepository.save(appUser);

            // Supprimer l'enregistrement de l'utilisateur
            appUserRepository.delete(appUser);

            return new ResponseEntity<>("Utilisateur supprimé avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
        }
    }


//    @Override
//    public AppUserResponseDTO addRoleToUser(String username, String roleName) {
//        AppUser user = appUserRepository.findByuserName(username);
//        if (user == null) {
//            throw new RuntimeException("Utilisateur introuvable");
//        }
//        System.out.println("l'utilisateur est :" + user);
//        AppRole role = appRoleRepository.findByroleName(roleName);
//
//        if (role == null) {
//            throw new RuntimeException("Rôle introuvable");
//        }
//        System.out.println("le rôle est :" + role);
//        user.getRoles().add(role);
//        System.out.println("la liste des rôles est :" + user.getRoles());
//        appUserRepository.save(user);
//        appRoleRepository.save(role);
//
//        // Convertir l'objet utilisateur mis à jour en DTO de réponse
//        return appUserMapper.modelMapper().map(user, AppUserResponseDTO.class);
//    }

    @Override
    public void  addRoleToUser(String userName, String roleName) {
        AppUser user = appUserRepository.findByNom(userName);
        if (user == null) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        AppRole role = appRoleRepository.findByrolename(roleName);

        if (role == null) {
            throw new RuntimeException("Rôle introuvable");
        }

        user.getRoles().add(role);
        appUserRepository.save(user);
        appRoleRepository.save(role);


    }




    @Override
    public AppUser  getUserByName(String username) {
        return appUserRepository.findByNom(username);

    }

    private AppUserResponseDTO getAppUserResponseDTO(AppUser user) {
        Set<Long> roleIds = user.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet());
        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(user, AppUserResponseDTO.class);
        if (user.getSociete() != null) {
            appUserResponseDTO.setSocieteId(user.getSociete().getId());
        }
        appUserResponseDTO.setRolesIds(roleIds);

        return appUserResponseDTO;
    }

}

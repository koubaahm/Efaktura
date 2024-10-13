package org.ms.authentificationservice.services;

import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.AppUserResponseDTO;
import org.ms.authentificationservice.entities.Abonnement;
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
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final AppUserMapper appUserMapper;

    private final AppRoleRepository appRoleRepository;

    private final SocieteRepository societeRepository;

    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(AppUserRepository appUserRepository, AppUserMapper appUserMapper, SocieteRepository societeRepository, AppRoleRepository appRoleRepository, PasswordEncoder passwordEncoder) {

        this.appUserRepository = appUserRepository;
        this.appUserMapper = appUserMapper;
        this.societeRepository = societeRepository;
        this.appRoleRepository = appRoleRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public List<AppUserResponseDTO> list() {
        List<AppUser> appUsers = appUserRepository.findAll();
        List<AppUserResponseDTO> appUserResponseDTOS = new ArrayList<>();

        for (AppUser appUser : appUsers) {
            AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
            appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
            appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
            if (appUser.getSociete() != null) {
                appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
            }
            if (appUser.getMySociete() != null) {
                appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
            }


            appUserResponseDTOS.add(appUserResponseDTO);
        }
        return appUserResponseDTOS;
    }

    @Override
    public List<AppUserResponseDTO> listBySocieteId(Long societeId) {
        Societe societe = societeRepository.findById(societeId).orElse(null);
        List<AppUser> appUsers = appUserRepository.findAllBySociete(societe);
        List<AppUserResponseDTO> appUserResponseDTOS = new ArrayList<>();

        for (AppUser appUser : appUsers) {
            AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
            appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
            appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
            if (appUser.getSociete() != null) {
                appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
            }
            if (appUser.getMySociete() != null) {
                appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
            }

            appUserResponseDTOS.add(appUserResponseDTO);
        }
        return appUserResponseDTOS;
    }


    @Override
    public AppUserResponseDTO getOne(Long id) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);
        AppUser appUser = optionalAppUser.orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable"));
        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
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
    public Abonnement getAbonnementByAdminUser(Long adminUserId) {
        AppUser adminUser = appUserRepository.findById(adminUserId).orElse(null);
        if (adminUser != null) {
            Societe societe = adminUser.getAdminSociete();
            if (societe != null) {
                return societe.getAbonnement();
            }
        }
        return null;
    }

    @Override
    public ResponseEntity<AppUserResponseDTO> save(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        if (appUserRequestDTO.getSocieteId() != null) {
            Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);

            appUser.setSociete(societe);
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
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AppUserResponseDTO> saveSuperAdmin(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        if (appUserRequestDTO.getSocieteId() != null) {
            Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);
            appUser.setSociete(societe);
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
        addRoleToUser(appUser.getNom(), "SUPER ADMIN");

        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);

        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<AppUserResponseDTO> saveAdmin(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        appUser.setSociete(null);

        appUser.setMySociete(null);

        Set<AppRole> appRoles = appUser.getRoles().stream()
                .map(appRoleRequestDTO -> appUserMapper.modelMapper().map(appRoleRequestDTO, AppRole.class))
                .collect(Collectors.toSet());

        appUser.setRoles(appRoles);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword())); // Crypter le mot de passe

        appUserRepository.save(appUser);
        addRoleToUser(appUser.getNom(), "ADMIN");

        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);

        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<AppUserResponseDTO> saveOperateur(AppUserRequestDTO appUserRequestDTO) {
        AppUser appUser = appUserMapper.modelMapper().map(appUserRequestDTO, AppUser.class);

        if (appUserRequestDTO.getSocieteId() != 0) {
            Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);
            appUser.setSociete(societe);
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
        addRoleToUser(appUser.getNom(), "OPERATEUR");

        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);

        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
        }
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));

        return new ResponseEntity<>(appUserResponseDTO, HttpStatus.CREATED);
    }


    @Override
    public ResponseEntity<AppUserResponseDTO> update(Long id, AppUserRequestDTO appUserRequestDTO) {
        Optional<AppUser> optionalAppUser = appUserRepository.findById(id);

        if (optionalAppUser.isPresent()) {
            AppUser appUserToUpdate = optionalAppUser.get();
            appUserToUpdate.setNom(appUserRequestDTO.getNom());
            appUserToUpdate.setPassword(appUserRequestDTO.getPassword());
            appUserToUpdate.setPassword(passwordEncoder.encode(appUserToUpdate.getPassword()));// crypter le password

            Set<AppRole> appRoles = appUserToUpdate.getRoles().stream()
                    .map(appRoleRequestDTO -> appUserMapper.modelMapper().map(appRoleRequestDTO, AppRole.class))
                    .collect(Collectors.toSet());
            appUserToUpdate.setRoles(appRoles);

            if (appRoles.stream().anyMatch(appRole -> appRole.getRolename().equals("SUPER ADMIN"))) {
                // L'utilisateur a le rôle "SuperAdmin", donc ne pas associer à une société
                appUserToUpdate.setSociete(null);
            } else {
                // Récupérer et associer la société correspondante
                Societe societe = societeRepository.findById(appUserRequestDTO.getSocieteId()).orElse(null);
                appUserToUpdate.setSociete(societe);
            }

            AppUser updatedAppUser = appUserRepository.save(appUserToUpdate);
            AppUserResponseDTO updatedAppUserResponseDTO = appUserMapper.modelMapper().map(updatedAppUser, AppUserResponseDTO.class);
            updatedAppUserResponseDTO.setRolesIds(updatedAppUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
            updatedAppUserResponseDTO.setRolesNom(updatedAppUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));
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


    @Override
    public void addRoleToUser(String userName, String roleName) {
        AppUser user = appUserRepository.findByNom(userName);
        if (user == null) {
            throw new RuntimeException("Utilisateur introuvable");
        }

        AppRole role = appRoleRepository.findByrolename(roleName);
        if (role == null) {
            throw new RuntimeException("Rôle introuvable");
        }

//        if (role.getRolename().equals("ADMIN") || role.getRolename().equals("SUPER ADMIN")) {
//            throw new RuntimeException("Impossible d'ajouter le rôle spécifié");
//        }

        user.getRoles().add(role);
        appUserRepository.save(user);
        appRoleRepository.save(role);
    }


    @Override
    public AppUser getUserByName(String username) {
        return appUserRepository.findByNom(username);

    }


    @Override
    public AppUserResponseDTO getUserByMail(String mail) {
        AppUser appUser = appUserRepository.findByEmail(mail);
        AppUserResponseDTO appUserResponseDTO = appUserMapper.modelMapper().map(appUser, AppUserResponseDTO.class);
        appUserResponseDTO.setRolesIds(appUser.getRoles().stream().map(AppRole::getId).collect(Collectors.toSet()));
        appUserResponseDTO.setRolesNom(appUser.getRoles().stream().map(AppRole::getRolename).collect(Collectors.toSet()));


        if (appUser.getSociete() != null) {
            appUserResponseDTO.setSocieteId(appUser.getSociete().getId());
        }
        if (appUser.getMySociete() != null) {
            appUserResponseDTO.setMySocieteId(appUser.getMySociete().getId());
            appUserResponseDTO.setAbonnementId(appUser.getMySociete().getAbonnement().getId());
        }

        return appUserResponseDTO;
    }


}

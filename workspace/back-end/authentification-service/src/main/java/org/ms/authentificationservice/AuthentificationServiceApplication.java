package org.ms.authentificationservice;

import org.ms.authentificationservice.dtos.AbonnementRequestDTO;
import org.ms.authentificationservice.dtos.AppRoleRequestDTO;
import org.ms.authentificationservice.dtos.AppUserRequestDTO;
import org.ms.authentificationservice.dtos.SocieteRequestDTO;
import org.ms.authentificationservice.entities.AppUser;
import org.ms.authentificationservice.webService.AbonnementRestController;
import org.ms.authentificationservice.webService.AppRoleRestController;
import org.ms.authentificationservice.webService.AppUserRestController;
import org.ms.authentificationservice.webService.SocieteRestController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;




@SpringBootApplication
public class AuthentificationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthentificationServiceApplication.class, args);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner start(AppUserRestController appUserRestController, AppRoleRestController appRoleRestController, SocieteRestController societeRestController, AbonnementRestController abonnementRestController, RepositoryRestConfiguration repositoryRestConfiguration) {
		repositoryRestConfiguration.exposeIdsFor(AppUser.class);
		return args -> {
			// Créer un rôle
			AppRoleRequestDTO appRoleRequestDTO = AppRoleRequestDTO.builder().rolename("SuperAdmin").build();
			AppRoleRequestDTO appRoleRequestDTO1 = AppRoleRequestDTO.builder().rolename("Admin").build();
			AppRoleRequestDTO appRoleRequestDTO2 = AppRoleRequestDTO.builder().rolename("Operateur").build();

			appRoleRestController.save(appRoleRequestDTO);
			appRoleRestController.save(appRoleRequestDTO1);
			appRoleRestController.save(appRoleRequestDTO2);

			// Créer un utilisateur
			AppUserRequestDTO user1 = AppUserRequestDTO.builder().nom("koubaa").email("koubaa@gmail.com").password("123").adresse("sfax").telephone("97551454").societeId(null).build();
			AppUserRequestDTO user2 = AppUserRequestDTO.builder().nom("mlik").email("mlik@gmail.com").password("456").adresse("sfax").telephone("23221441").societeId(null).build();
			AppUserRequestDTO user3= AppUserRequestDTO.builder().nom("ali").email("ali@gmail.com").password("789").adresse("tunis").telephone("74552214").societeId(null).build();
			AppUserRequestDTO user4 = AppUserRequestDTO.builder().nom("mohamed").email("mohamed@gmail.com").password("1234").adresse("sfax").telephone("28221481").societeId(null).build();
			AppUserRequestDTO user5 = AppUserRequestDTO.builder().nom("maryem").email("maryem@gmail.com").password("4567").adresse("sfax").telephone("23855141").societeId(null).build();

			appUserRestController.save(user1);
			appUserRestController.save(user2);
			appUserRestController.save(user3);
			appUserRestController.save(user4);
			appUserRestController.save(user5);

			System.out.println("Utilisateur 1 : " + appUserRestController.getOne(1L));

			// Ajouter un rôle à un utilisateur
			appUserRestController.addRoleToUser("koubaa", "SuperAdmin");
			appUserRestController.addRoleToUser("mlik", "SuperAdmin");
			appUserRestController.addRoleToUser("ali", "Admin");
			appUserRestController.addRoleToUser("ali", "Operateur");
			appUserRestController.addRoleToUser("mohamed", "Admin");
			appUserRestController.addRoleToUser("maryem", "Admin");



			//System.out.println("Utilisateur 1 : " + appUserRestController.getUserByName("Ahmed"));
			// Créer un abonnement
			AbonnementRequestDTO abonnement =AbonnementRequestDTO.builder().build();
			AbonnementRequestDTO abonnement2 =AbonnementRequestDTO.builder().build();
			AbonnementRequestDTO abonnement3 =AbonnementRequestDTO.builder().build();

			abonnementRestController.save(abonnement);
			abonnementRestController.save(abonnement2);
			abonnementRestController.save(abonnement3);
			System.out.println("abonnement 1 :"+abonnementRestController.getOne(1L));
			System.out.println("***********************");
			System.out.println("Utilisateur 3 : " + appUserRestController.getOne(3L));
			// Créer une société
			SocieteRequestDTO societe1 = SocieteRequestDTO.builder().nom("C holding").adminUserId(3L).email("cHolding@gmail.com").adresse("tunis").abonnementId(1L).matriculeFiscal("ma8854114").telephone("71552114").build();
			SocieteRequestDTO societe2 = SocieteRequestDTO.builder().nom("Ideryet").adminUserId(4L).email("Ideryet@gmail.com").adresse("sfax").abonnementId(2L).matriculeFiscal("ma888441").telephone("7455889741").build();
			SocieteRequestDTO societe3 = SocieteRequestDTO.builder().nom("PC House").adminUserId(5L).email("PCHouse@gmail.com").adresse("sousse").abonnementId(3L).matriculeFiscal("ma81444").telephone("73214411").build();
			societeRestController.save(societe1);
			societeRestController.save(societe2);
			societeRestController.save(societe3);
			AppUserRequestDTO user6= AppUserRequestDTO.builder().nom("salah").email("salah@gmail.com").password("012").adresse("tunis").telephone("7321144").societeId(1L).build();
			appUserRestController.save(user6);
			appUserRestController.addRoleToUser("salah","Operateur");
//			System.out.println("societe 1 :"+societeRestController.getOne(1L));
			// Ajouter l'utilisateur et le rôle à la société

			// Ajouter le rôle à l'utilisateur
		};
	}


}

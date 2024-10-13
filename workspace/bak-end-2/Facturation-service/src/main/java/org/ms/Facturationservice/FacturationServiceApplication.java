package org.ms.Facturationservice;

import org.ms.Facturationservice.dtos.*;
import org.ms.Facturationservice.controllers.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class FacturationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FacturationServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(ClientRestController clientRestController, FournisseurRestController fournisseurRestController, ProduitRestController produitRestController, FactureRestController factureRestController, LigneFactureRestController ligneFactureRestController, TvaRestController tvaRestController, LigneAchatRestController ligneAchatRestController) {
		return args ->
		{


			// Insérer tva de test dans la BD
//			TvaRequestDto tva0=TvaRequestDto.builder().label("0%").valeur(0.0).societeId(1L).build();
//			TvaRequestDto tva7=TvaRequestDto.builder().label("7%").valeur(0.7).societeId(1L).build();
//			TvaRequestDto tva19=TvaRequestDto.builder().label("19%").valeur(1.9).societeId(1L).build();
//			tvaRestController.save(tva0);
//			tvaRestController.save(tva7);
//			tvaRestController.save(tva19);
//			System.out.println(tva0);
//			System.out.println(tva7);
//			System.out.println(tva19);
//
//			// Insérer produit de test dans la BD
//
//			ProduitRequestDto p1 = new ProduitRequestDto().builder().id(null).label("dell").description("nouveau produit").quantite(20).marge(0.3).tvaId(2L).societeId(1L).build();
//			System.out.println(p1);
//			produitRestController.save(p1);
//			ProduitRequestDto p2 = new ProduitRequestDto().builder().id(null).label("hp").description("nouveau produit").tvaId(2L).marge(0.3).quantite(80).societeId(1L).build();
//			System.out.println(p2);
//			produitRestController.save(p2);
//
//			// Insérer trois clients de test dans la BD
//
//			ClientRequestDto client1= ClientRequestDto.builder().id(null).nom("ali").adresse("tunis").cin_MatriculeFiscal("0882544111").email("Ali.ms@gmail.com").telephone(24744144L).societeId(1L).build();
//			System.out.println(client1);
//			clientRestController.save(client1);
//
//			ClientRequestDto client2= ClientRequestDto.builder().id(null).nom("Mariem").adresse("sfax").cin_MatriculeFiscal("0851112211").email("Mariem.ms@gmail.com").telephone(22547854L).societeId(1L).build();
//			System.out.println(client2);
//			clientRestController.save(client2);
//
//			ClientRequestDto client3= ClientRequestDto.builder().id(null).nom("Mohamed").adresse("sfax").cin_MatriculeFiscal("088544412").email("Mohamed.ms@gmail.com").telephone(54211444L).societeId(1L).build();
//			clientRestController.save(client3);
//			System.out.println(client3);
//
//			// Insérer facture de test dans la BD
//
//			FactureRequestDto factureRequestDto1=FactureRequestDto.builder().id(null).factureLignes(new ArrayList<>()).clientId(1L).commentaires("RS").modePaiement("RS").statut("paye").societeId(1L).build();
//			factureRestController.save(factureRequestDto1);
//			System.out.println(factureRequestDto1);
//			FactureRequestDto factureRequestDto2=FactureRequestDto.builder().id(null).factureLignes(new ArrayList<>()).clientId(1L).commentaires("RS").modePaiement("RS").statut("paye").societeId(1L).build();
//			factureRestController.save(factureRequestDto2);
//			System.out.println(factureRequestDto2);
//
//			// Insérer Lignefacture de test dans la BD
//
//			LigneFactureRequestDto factureLigneRequestDto=LigneFactureRequestDto.builder().id(null).factureId(1L).produitId(1L).prixUnitaire(14.5).tauxRemise(7).quantite(12).build();
//			System.out.println(factureLigneRequestDto);
//			//ligneFactureRestController.save(factureLigneRequestDto);
//			LigneFactureRequestDto factureLigneRequestDto2=LigneFactureRequestDto.builder().id(null).factureId(1L).produitId(2L).prixUnitaire(14.5).tauxRemise(7).quantite(40).build();
//			System.out.println(factureLigneRequestDto2);
//			//ligneFactureRestController.save(factureLigneRequestDto2);
//
//			ArrayList<LigneFactureRequestDto> ligneFactureRequestDtoList = new ArrayList<>();
//			ligneFactureRequestDtoList.add(factureLigneRequestDto2);
//			ligneFactureRequestDtoList.add(factureLigneRequestDto);
//
////			List<LigneFactureResponseDto> ligneFactureResponseDtos = ligneFactureRestController.saveList(ligneFactureRequestDtoList);
////
////			for (LigneFactureResponseDto ligne : ligneFactureResponseDtos) {
////				System.out.println("/////////////" + ligne.getRemise());
////			}
//
//			// Insérer fournisseur de test dans la BD
//
//			FournisseurRequestDto fournisseurRequestDto=FournisseurRequestDto.builder().id(null).nom("Computer House").adresse("sfax").telephone("74552141").societeId(1L).build();
//			FournisseurRequestDto fournisseurRequestDto1=FournisseurRequestDto.builder().id(null).nom("ideryet").adresse("sfax").telephone("74944145").societeId(1L).build();
//
//
//
//			fournisseurRestController.save(fournisseurRequestDto);
//			fournisseurRestController.save(fournisseurRequestDto1);
//
//			// Insérer ligneAchat de test dans la BD
//
//			LigneAchatRequestDto ligneAchatRequestDto=LigneAchatRequestDto.builder().id(null).fournisseurId(1L).produitId(2L).quantiteAchat(20).prixUnitaire(19.5).societeId(1L).build();
//			LigneAchatRequestDto ligneAchatRequestDto1=LigneAchatRequestDto.builder().id(null).fournisseurId(2L).produitId(2L).quantiteAchat(40).prixUnitaire(20.5).societeId(1L).build();
//			LigneAchatRequestDto ligneAchatRequestDto2=LigneAchatRequestDto.builder().id(null).fournisseurId(2L).produitId(2L).quantiteAchat(80).prixUnitaire(20.5).societeId(1L).build();
//			ligneAchatRestController.save(ligneAchatRequestDto);
//			ligneAchatRestController.save(ligneAchatRequestDto1);
//			ligneAchatRestController.save(ligneAchatRequestDto2);
//
//			List<LigneAchatResponseDto> ligneAchatResponseDtos=ligneAchatRestController.getAllByProduit(2L);
//
//			for (LigneAchatResponseDto ligne : ligneAchatResponseDtos) {
//				System.out.println("/////////////" + ligne.getProduitId());
//			}








		};
	}

}

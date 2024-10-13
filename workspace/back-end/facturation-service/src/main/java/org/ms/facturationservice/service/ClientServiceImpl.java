package org.ms.Facturationservice.service;



import org.ms.Facturationservice.dto.ClientRequestDto;
import org.ms.Facturationservice.dto.ClientResponseDto;
import org.ms.Facturationservice.entities.Client;
import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.ClientMapperConfig;
import org.ms.Facturationservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {
    @Autowired
    private ClientRepository clientRepository;
   @Autowired
   ClientMapperConfig clientMapperConfig;

@Autowired
   SocieteServiceFeign societeServiceFeign;

    @Override
    public List<ClientResponseDto> list() {
        List<Client> clients = clientRepository.findAll();
        List<ClientResponseDto> clientsDTO = new ArrayList<>();

        for (Client client : clients) {
            ClientResponseDto clientDTO = clientMapperConfig.modelMapper().map(client, ClientResponseDto.class);
            clientDTO.setFactureIds(client.getFactures().stream().map(Facture::getId).collect(Collectors.toList()));
            clientsDTO.add(clientDTO);
        }


        return clientsDTO;
    }
    @Override
    public List<ClientResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {
            List<Client> clients = clientRepository.findBySocieteId(societeId);
            List<ClientResponseDto> clientsDTO = new ArrayList<>();

            for (Client client : clients) {
                ClientResponseDto clientDTO = clientMapperConfig.modelMapper().map(client, ClientResponseDto.class);
                clientDTO.setFactureIds(client.getFactures().stream().map(Facture::getId).collect(Collectors.toList()));
                clientsDTO.add(clientDTO);
            }

            return clientsDTO;
        }
    }


    @Override
    public ClientResponseDto getOne(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);
        Client client = optionalClient.orElseThrow(() -> new EntityNotFoundException("Client not found"));

        List<Long> factureIds = client.getFactures().stream().map(Facture::getId).collect(Collectors.toList());

        ClientResponseDto clientResponseDto = clientMapperConfig.modelMapper().map(client, ClientResponseDto.class);
        clientResponseDto.setFactureIds(factureIds);

        return clientResponseDto;
    }

    @Override
    public ResponseEntity<ClientResponseDto> save(ClientRequestDto clientRequestDto) {

        if (societeServiceFeign.getOne(clientRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + clientRequestDto.getSocieteId());
        } else {
            Client client = clientMapperConfig.modelMapper().map(clientRequestDto, Client.class); // convertir le DTO client en objet Client

            clientRepository.save(client); // ajouter le client à la base de données
            ClientResponseDto newClientResponseDto = clientMapperConfig.modelMapper().map(client, ClientResponseDto.class); // convertir le nouveau client en DTO client

            return new ResponseEntity<>(newClientResponseDto, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity<ClientResponseDto> update(Long id, ClientRequestDto clientRequestDto) {
        if (societeServiceFeign.getOne(clientRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + clientRequestDto.getSocieteId());
        } else {
            Optional<Client> optionalClient = clientRepository.findById(id);
            if (optionalClient.isPresent()) {
                Client client = optionalClient.get();
                clientMapperConfig.modelMapper().map(clientRequestDto, client); // mettre à jour les champs du client avec les valeurs du DTO
                clientRepository.save(client);// mettre à jour le client dans la base de données
                ClientResponseDto updatedClientResponseDto = clientMapperConfig.modelMapper().map(client, ClientResponseDto.class);// convertir le client mis à jour en DTO client
                return new ResponseEntity<>(updatedClientResponseDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Client> optionalClient = clientRepository.findById(id);

        if (optionalClient.isPresent()) { // si le client existe
            clientRepository.deleteById(id); // supprimer le client
            return new ResponseEntity<>("Client supprimée avec succès.", HttpStatus.OK);
        } else { // si le client n'existe pas
            return new ResponseEntity<>("Client non trouvée.", HttpStatus.NOT_FOUND);
        }
    }
}

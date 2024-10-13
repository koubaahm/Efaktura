package org.ms.Facturationservice.service;



import org.ms.Facturationservice.dto.FactureRequestDto;
import org.ms.Facturationservice.dto.FactureResponseDto;
import org.ms.Facturationservice.entities.Client;
import org.ms.Facturationservice.entities.Facture;
import org.ms.Facturationservice.entities.LigneFacture;
import org.ms.Facturationservice.entities.SocieteResponseDTO;
import org.ms.Facturationservice.feign.SocieteServiceFeign;
import org.ms.Facturationservice.mapper.FactureMapperConfig;
import org.ms.Facturationservice.repository.ClientRepository;
import org.ms.Facturationservice.repository.FactureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class FactureServiceImpl implements FactureService{
    @Autowired
    private FactureRepository factureRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Autowired
    FactureMapperConfig factureMapperConfig;

    @Autowired
    SocieteServiceFeign societeServiceFeign;


    @Override
    public List<FactureResponseDto> list() {
        List<Facture> factures = factureRepository.findAll();
        List<FactureResponseDto> factureResponseDtos= new ArrayList<>();

        for (Facture facture : factures) {
            FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(facture, FactureResponseDto.class);
            factureResponseDto.setFactureLigneIds(facture.getLigneFactures().stream().map(LigneFacture::getId).collect(Collectors.toList()));
            factureResponseDto.setSocieteId(facture.getSocieteId());
            factureResponseDto.setClientId(facture.getClient().getId());
            factureResponseDto.setId(facture.getId());
            factureResponseDtos.add(factureResponseDto);
        }
        return factureResponseDtos;
    }
    @Override
    public List<FactureResponseDto> listBySocieteId(Long societeId) {
        if (societeServiceFeign.getOne(societeId) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + societeId);
        } else {
            List<Facture> factures = factureRepository.findBySocieteId(societeId);
            List<FactureResponseDto> factureResponseDtos = new ArrayList<>();

            for (Facture facture : factures) {
                FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(facture, FactureResponseDto.class);
                factureResponseDto.setFactureLigneIds(facture.getLigneFactures().stream().map(LigneFacture::getId).collect(Collectors.toList()));
                factureResponseDto.setSocieteId(facture.getSocieteId());
                factureResponseDto.setClientId(facture.getClient().getId());
                factureResponseDto.setId(facture.getId());
                factureResponseDtos.add(factureResponseDto);
            }

            return factureResponseDtos;
        }
    }


    @Override
    public List<FactureResponseDto> listFacturesPaye() {
        List<Facture> factures = factureRepository.findAll();
        return factures.stream()
                .filter(facture -> facture.getStatut().equals("paye"))
                .map(facture -> {
                    FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(facture, FactureResponseDto.class);
                    factureResponseDto.setClientId(facture.getClient().getId());
                    factureResponseDto.setSocieteId(facture.getSocieteId());
                    factureResponseDto.setId(facture.getId());
                    factureResponseDto.setFactureLigneIds(facture.getLigneFactures()
                            .stream().map(LigneFacture::getId).collect(Collectors.toList()));
                    return factureResponseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public FactureResponseDto getOne(Long id) {
        Optional<Facture> optionalFacture = factureRepository.findById(id);
        Facture facture = optionalFacture.orElseThrow(() -> new EntityNotFoundException("Facture not found"));
        List<Long> factureLigneIds = facture.getLigneFactures().stream().map(LigneFacture::getId).collect(Collectors.toList());

        FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(facture, FactureResponseDto.class);
        factureResponseDto.setClientId(facture.getClient().getId());
        factureResponseDto.setSocieteId(facture.getSocieteId());
        factureResponseDto.setFactureLigneIds(factureLigneIds);

        return factureResponseDto;
    }

@Override
public ResponseEntity<FactureResponseDto> save(FactureRequestDto factureRequestDto) {
    if (societeServiceFeign.getOne(factureRequestDto.getSocieteId()) == null) {

        throw new RuntimeException("Société introuvable avec l'ID: " + factureRequestDto.getSocieteId());
    } else {
        Facture facture = factureMapperConfig.modelMapper().map(factureRequestDto, Facture.class);
        Client client = clientRepository.findById(factureRequestDto.getClientId()).orElse(null);
        facture.setClient(client);

        facture.setSocieteId(factureRequestDto.getSocieteId());

        List<LigneFacture> ligneFactures = factureRequestDto.getFactureLignes().stream()
                .map(factureLigneRequestDto -> factureMapperConfig.modelMapper().map(factureLigneRequestDto, LigneFacture.class))
                .peek(factureLigne -> factureLigne.setFacture(facture))
                .collect(Collectors.toList());

        facture.setLigneFactures(ligneFactures);
        facture.setNumeroFacture(generateNumeroFacture(factureRequestDto.getSocieteId()));
        factureRepository.save(facture);


        FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(facture, FactureResponseDto.class);
        List<Long> ligneFactureIds = facture.getLigneFactures().stream().map(LigneFacture::getId).collect(Collectors.toList());
        factureResponseDto.setFactureLigneIds(ligneFactureIds);
        factureResponseDto.setClientId(client.getId());

        return new ResponseEntity<>(factureResponseDto, HttpStatus.CREATED);
    }
}


    @Override
    public FactureResponseDto update(Long id, FactureRequestDto factureRequestDto) {
        if (societeServiceFeign.getOne(factureRequestDto.getSocieteId()) == null) {

            throw new RuntimeException("Société introuvable avec l'ID: " + factureRequestDto.getSocieteId());
        } else {
            Facture factureToUpdate = factureRepository.findById(id).get();
            factureToUpdate.setModePaiement(factureRequestDto.getModePaiement());
            factureToUpdate.setStatut(factureRequestDto.getStatut());
            factureToUpdate.setCommentaires(factureRequestDto.getCommentaires());

            Client client = clientRepository.findById(factureRequestDto.getClientId()).get();
            factureToUpdate.setClient(client);
            SocieteResponseDTO societeResponseDTO = societeServiceFeign.getOne(factureRequestDto.getSocieteId());
            factureToUpdate.setSocieteId(societeResponseDTO.getId());
            factureRequestDto.getFactureLignes().stream()
                    .map(factureLigneRequestDto -> factureMapperConfig.modelMapper().map(factureLigneRequestDto, LigneFacture.class))
                    .peek(factureLigne -> factureLigne.setFacture(factureToUpdate))
                    .collect(Collectors.toList());


            Facture updatedFacture = factureRepository.save(factureToUpdate);
            FactureResponseDto factureResponseDto = factureMapperConfig.modelMapper().map(updatedFacture, FactureResponseDto.class);
            List<Long> ligneFactureIds = updatedFacture.getLigneFactures().stream().map(LigneFacture::getId).collect(Collectors.toList());
            factureResponseDto.setFactureLigneIds(ligneFactureIds);
            return factureResponseDto;
        }
    }



    @Override
    public ResponseEntity<?> delete (Long id) {
        Facture facture = factureRepository.getReferenceById(id);
        if (facture != null) {
            factureRepository.delete(facture);
            return new ResponseEntity<>("Facture supprimée avec succès.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Facture non trouvée.", HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public String generateNumeroFacture(Long idSoc) {
        Facture lastNumeroFacture = factureRepository.findLastFactureBySocieteId(idSoc);

        StringBuilder sb = new StringBuilder();
        sb.append("FA");

        int currentYear = LocalDate.now().getYear() % 100;
        sb.append(String.format("%02d%02d", currentYear, idSoc));

        if (lastNumeroFacture != null) {
            String lastFactureYear = lastNumeroFacture.getNumeroFacture().substring(2, 4);
            String currentYearStr = String.format("%02d", currentYear);

            if (!lastFactureYear.equals(currentYearStr)) {
                sb.append("01");
            } else {
                String lastFactureNumber = lastNumeroFacture.getNumeroFacture().substring(6);
                int nextFactureNumber = Integer.parseInt(lastFactureNumber) + 1;
                sb.append(String.format("%02d", nextFactureNumber));
            }
        } else {
            sb.append("01");
        }

        return sb.toString();
    }

   public String getlastNumeroFactureBySociete(Long idSoc){

        Facture lastNumeroFacture = factureRepository.findLastFactureBySocieteId(idSoc);

        return lastNumeroFacture.getNumeroFacture();
    }





}

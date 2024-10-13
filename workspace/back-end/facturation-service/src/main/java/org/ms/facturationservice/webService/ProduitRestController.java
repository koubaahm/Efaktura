package org.ms.Facturationservice.webService;



import org.ms.Facturationservice.dto.ProduitRequestDto;
import org.ms.Facturationservice.dto.ProduitResponseDto;
import org.ms.Facturationservice.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/produits")
//@CrossOrigin(origins = "http://localhost:3000")
public class ProduitRestController {
    @Autowired
    ProduitService produitService;

    @GetMapping
    public List<ProduitResponseDto> list() {
        return produitService.list();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProduitResponseDto> getOne(@PathVariable Long id) {
        return produitService.getOne(id);
    }

    @PostMapping
    public ResponseEntity<ProduitResponseDto> save(@RequestBody ProduitRequestDto produitRequestDto) {
        return produitService.save(produitRequestDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProduitResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProduitRequestDto produitRequestDto) {
        return produitService.update(id, produitRequestDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return produitService.delete(id);
    }
    @GetMapping("bySociete/{id}")
    public List<ProduitResponseDto> listBySocieteId(@PathVariable Long id){
        return  produitService.listBySocieteId(id);

    }
}

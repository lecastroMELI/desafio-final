package br.com.meli.desafio_final.controller;


import br.com.meli.desafio_final.dto.*;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.enums.Category;
import br.com.meli.desafio_final.service.implementation.AdsenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adsense")
public class AdsenseController {

    @Autowired
    private AdsenseService adsenseService;

    /**
     * Nesse método retornamos uma lista de anúncios
     * @return
     */
    // TODO: colocar DTO
    @GetMapping
    public ResponseEntity<List<AdsenseDto>> findAll() {
        return ResponseEntity.ok(AdsenseDto.convertListDto(adsenseService.findAll()));
    }

    /**
     * Nesse método retornamos anúncio listado por categoria
     * @param querytype
     * @return
     */
    // TODO: colocar DTO
    @GetMapping("/list")
    public ResponseEntity<List<AdsenseDto>> findByCategory(@RequestParam Category querytype) {
        return ResponseEntity.ok(AdsenseDto.convertListDto(adsenseService.findByCategory(querytype)));
    }

    /**
     * Nesse método retornamos um lista de anúncio por armazém / ou a qual armazém esse anúncio pertece
     * @param adsenseId
     * @return
     */
    @GetMapping("/warehouse/{adsenseId}")
    public ResponseEntity <List<AdsenseByWarehouseDto>> getByAdsenseByWarehouse(@PathVariable Long adsenseId) {
        return ResponseEntity.status(HttpStatus.OK).body(adsenseService.findAdsenseByWarehouseAndQuantity(adsenseId));
    }


    @PostMapping("/insert")
    public ResponseEntity<AdsenseInsertDto> createAdsense(@RequestBody Adsense newAdsense) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsenseService.insertAdsense(newAdsense));
    }

    @GetMapping("/{adsenseId}")
    public ResponseEntity<AdsenseDto> readAdsenseById(@PathVariable Long adsenseId) {
        return ResponseEntity.status(HttpStatus.OK).body(AdsenseDto.convertDto(adsenseService.findById(adsenseId)));
    }

    @PutMapping("/{adsenseId}")
    public ResponseEntity<AdsenseUpdateDto> updateAdsense(
        @RequestBody Adsense adsense,
        @PathVariable Long adsenseId,
        @RequestParam Long sellerId) {
        return ResponseEntity.status(HttpStatus.OK).body(
            adsenseService.updateAdsenseById(adsense, adsenseId, sellerId));
    }

    @DeleteMapping("/{adsenseId}")
    public ResponseEntity<Void> deleteAdsense(@PathVariable Long adsenseId) {
        this.adsenseService.deleteAdsenseById(adsenseId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}


package br.com.meli.desafio_final.controller;

import br.com.meli.desafio_final.dto.AdsenseByWarehouseDto;
import br.com.meli.desafio_final.dto.AdsenseDto;
import br.com.meli.desafio_final.dto.AdsenseInsertDto;
import br.com.meli.desafio_final.dto.AdsenseUpdateDto;
import br.com.meli.desafio_final.exception.NotFound;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.enums.Category;
import br.com.meli.desafio_final.service.implementation.AdsenseService;
import br.com.meli.desafio_final.util.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;

import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdsenseControllerTest {

    @InjectMocks
    private AdsenseController adsenseController;

    @Mock
    private AdsenseService adsenseService;

    // TODO: REMOVER A PALAVRA "TEST" DOS NOMES DOS MÉTODOS, POIS A MAIORIA NÃO POSSUI
    // TODO: ADICIONAR @DisplayName() AOS TESTES QUE NÃO O POSSUI
    // TODO: ADICIONAR O public AOS MÉTODOS

    @Test
    public void find_findByCategory_whenAdsensesByCategoryExist() {
        List<AdsenseDto> adsenseList = AdsenseUtilsDto.generateAdsenseDtoList();
        BDDMockito.when(adsenseService.findByCategory(ArgumentMatchers.any(Category.class)))
                .thenReturn(AdsenseUtils.generateAdsenseList());

        ResponseEntity<List<AdsenseDto>> response = adsenseController.findByCategory(Category.FRESH);

        verify(adsenseService, atLeastOnce()).findByCategory(Category.FRESH);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        // TODO: AJUSTAR O IMPORT
        assertEquals(response.getBody().size(), 3);
        assertEquals(response.getBody().get(0).getPrice(), adsenseList.get(0).getPrice());
        assertEquals(response.getBody().get(0).getProduct().getId(), adsenseList.get(0).getProduct().getId());
        assertEquals(response.getBody().get(0).getSeller().getId(), adsenseList.get(0).getSeller().getId());
    }

    @Test
    @DisplayName("Listar anúncios: Valida se retorna uma lista de anúncios.")
    public void findAll_returnListAdsense_whenAdsensesExists() {
        BDDMockito.when(adsenseService.findAll())
            .thenReturn(AdsenseUtils.generateAdsenseList());

        AdsenseDto adsenseDto = AdsenseUtilsDto.newAdsense1ToSave();

        ResponseEntity<List<AdsenseDto>> response = adsenseController.findAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isPositive().isEqualTo(3);
        assertEquals(response.getBody().get(0).getPrice(), adsenseDto.getPrice());
        assertEquals(response.getBody().get(0).getProduct().getId(), adsenseDto.getProduct().getId());
        assertEquals(response.getBody().get(0).getSeller().getId(), adsenseDto.getSeller().getId());
    }

    @Test
    @DisplayName("Listar anúncios: Valida se dispara a execeção NOT FOUND quando não há anúncios cadastrados.")
    public void findAll_throwException_whenAdsensesNotExists() {
        BDDMockito.when(adsenseService.findAll())
            .thenAnswer((invocationOnMock) -> {
                throw new NotFound("💢 Lista de anúncios não encontrada");
            });

        Exception exception = null;
        try {
            adsenseController.findAll();
        } catch (NotFound ex) {
            exception = ex;
        }

        verify(adsenseService, atLeastOnce()).findAll();
        assertThat(exception.getMessage()).isEqualTo("💢 Lista de anúncios não encontrada");
    }

    @Test
    public void testGetByAdsenseByWarehouse() {
        long adsenseId = AdsenseUtils.newAdsense1ToSave().getId();
        BDDMockito.when(adsenseService.findAdsenseByWarehouseAndQuantity(adsenseId))
                .thenReturn(AdsenseByWarehouseDtoUtils.AdsenseByWarehouseDtoListDto());

        ResponseEntity <List<AdsenseByWarehouseDto>> response = adsenseController.getByAdsenseByWarehouse(adsenseId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isNotNull().isPositive().isEqualTo(4);
    }

    @Test
    void createAdsense() {
        Adsense newAdsense = AdsenseUtils.newAdsense3ToSave();

        BDDMockito.when(adsenseService.insertAdsense(newAdsense))
            .thenReturn(AdsenseInsertDtoUtils.adsenseWithId());

        ResponseEntity<AdsenseInsertDto> response = adsenseController.createAdsense(newAdsense);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAdsenseId()).isEqualTo(AdsenseInsertDtoUtils.adsenseWithId().getAdsenseId());
    }

    @Test
    void readAdsenseById() {
        Long adsenseId = AdsenseUtils.adsenseWithId().getId();

        BDDMockito.when(adsenseService.findById(anyLong()))
            .thenReturn(AdsenseUtils.adsenseWithId());

        ResponseEntity<AdsenseDto> response = adsenseController.readAdsenseById(adsenseId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateAdsense() {
        Adsense adsense = AdsenseUtils.adsenseWithId();
        Long adsenseId = AdsenseUtils.adsenseWithId().getId();
        Long sellerId = SellerUtils.newSeller3ToSave().getId();

        BDDMockito.when(adsenseService.updateAdsenseById(adsense, adsenseId, sellerId))
            .thenReturn(AdsenseUpdateDtoUtils.adsenseUpdated());

        ResponseEntity<AdsenseUpdateDto> response = adsenseController.updateAdsense(adsense, adsenseId, sellerId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isExactlyInstanceOf(AdsenseUpdateDto.class);
    }

    @Test
    void deleteAdsense() {
        Adsense adsense = AdsenseUtils.adsenseWithId();

        BDDMockito.doNothing()
            .when(adsenseService).deleteAdsenseById(anyLong());

        assertThatCode(() -> {
            adsenseController.deleteAdsense(adsense.getId());
        }).doesNotThrowAnyException();

        verify(adsenseService, atLeastOnce()).deleteAdsenseById(anyLong());
    }
}


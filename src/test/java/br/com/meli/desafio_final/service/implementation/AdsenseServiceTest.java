package br.com.meli.desafio_final.service.implementation;

import br.com.meli.desafio_final.dto.AdsenseByWarehouseDto;
import br.com.meli.desafio_final.dto.AdsenseIdDto;
import br.com.meli.desafio_final.dto.AdsenseInsertDto;
import br.com.meli.desafio_final.dto.AdsenseUpdateDto;
import br.com.meli.desafio_final.exception.BadRequest;
import br.com.meli.desafio_final.exception.NotFound;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.enums.Category;
import br.com.meli.desafio_final.repository.AdsenseRepository;
import br.com.meli.desafio_final.repository.SellerRepository;
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
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdsenseServiceTest {

    @InjectMocks
    private AdsenseService adsenseService;

    @Mock
    private AdsenseRepository adsenseRepository;

    @Mock
    private BatchService batchService;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private ProductService productService;

    // TODO: REMOVER A PALAVRA "TEST" DOS NOMES DOS MÉTODOS, POIS A MAIORIA NÃO POSSUI
    // TODO: ADICIONAR @DisplayName() AOS TESTES QUE NÃO O POSSUI
    // TODO: ADICIONAR O public AOS MÉTODOS

    @Test
    public void find_findByCategory_whenAdsensesByCategoryExist() {
        BDDMockito.when(adsenseRepository.findAll())
                .thenReturn(AdsenseUtils.generateAdsenseList());

        List<Adsense> adsenseList = adsenseService.findByCategory(Category.FRESH);

        Assertions.assertThat(adsenseList).isNotNull();
        Assertions.assertThat(adsenseList.size()).isEqualTo(2);
    }

    @Test
    public void find_findByCategory_whenAdsensesByCategoryDontExist() {
        BDDMockito.when(adsenseRepository.findAll()).thenReturn(Collections.emptyList());
        Exception exception = null;
        List<Adsense> adsenseList = null;
        try {
            adsenseList = adsenseService.findByCategory(Category.FRESH);
        } catch (Exception e) {
            exception = e;
        }
        verify(adsenseRepository, atLeastOnce()).findAll();
        Assertions.assertThat(adsenseList).isNull();
        assertThat(exception.getMessage()).isEqualTo("💢 Lista de anúncios não encontrada");
        // TODO: Mensagem do erro
    }

    @Test
    @DisplayName("Busca pelo ID: Valida se retorna um anúncio completo quando o ID é válido.")
    void findById_returnAdsense_whenIdIsValid() {
        BDDMockito.when(adsenseRepository.findById(anyLong()))
            .thenReturn(Optional.of(AdsenseUtils.newAdsense1ToSave()));

        Adsense adsense = AdsenseUtils.newAdsense1ToSave();

        Adsense adsenseFound = adsenseService.findById(1L);

        assertThat(adsenseFound).isNotNull();
        assertThat(adsenseFound.getId()).isEqualTo(adsense.getId());
    }

    @Test
    @DisplayName("Busca pelo ID: Valida se dispara a exceção NOT FOUND quando o ID é inválido.")
    void findById_throwException_whenIdInvalid() {
        assertThrows(NotFound.class, () -> {
           adsenseService.findById(0L);
        });
    }

    @Test
    @DisplayName("Listar anúncios: Valida se retorna uma lista de anúncios.")
    void findAll_returnListAdsense_whenAdsensesExists() {
        BDDMockito.when(adsenseRepository.findAll())
            .thenReturn(List.of(AdsenseUtils.newAdsense1ToSave()));

        List<Adsense> adsenseList = adsenseService.findAll();

        assertThat(adsenseList).isNotNull();
        assertThat(adsenseList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Listar anúncios: Valida se dispara a execeção NOT FOUND quando não há anúncios cadastrados.")
    void findAll_throwException_whenAdsensesNotExists() {
        assertThrows(NotFound.class, () -> {
            adsenseService.findAll();
        });
    }

    @Test
    void find_findAdsensesByProductId_whenSuccess() {
        BDDMockito.when(adsenseRepository.findAll())
                .thenReturn(List.of(AdsenseUtils.newAdsense1ToSave()));
        List<AdsenseIdDto> adsenseList = adsenseService.findByProductId(1L);
        List<AdsenseIdDto> newList = AdsenseIdDto.convertDto(List.of(AdsenseUtils.newAdsense1ToSave()));
        assertThat(adsenseList).isNotNull();
        assertThat(adsenseList.contains(newList));
    }


    @Test
    public void testFindAdsenseByWarehouseAndQuantity() {
        long adsenseId = AdsenseUtils.newAdsense1ToSave().getId();
        BDDMockito.when(batchService.getAdsenseByWarehouseAndQuantity(adsenseId))
                .thenReturn(AdsenseByWarehouseDtoUtils.AdsenseByWarehouseDtoListDto());

        List<AdsenseByWarehouseDto> adsenseList = adsenseService.findAdsenseByWarehouseAndQuantity(adsenseId);

        Assertions.assertThat(adsenseList).isNotNull();
        Assertions.assertThat(adsenseList.size()).isEqualTo(4);
    }


    @Test
    @DisplayName("CREATE: Retorna os dados do anúncio criado quando a inserção é com sucesso.")
    void insertAdsense_returnAdsense_whenInsertedWithSuccess() {
        Adsense newAdsense = AdsenseInsertDtoUtils.newAdsenseInsertToSave();

        BDDMockito.when(adsenseRepository.save(newAdsense))
            .thenReturn(AdsenseUtils.adsenseWithId());

        BDDMockito.when(productService.findById(anyLong()))
            .thenReturn(ProductUtils.newProduct5ToSave());

        BDDMockito.when(sellerRepository.findById(anyLong()))
            .thenReturn(Optional.of(SellerUtils.newSeller3ToSave()));

        AdsenseInsertDto adsenseInsertDto = adsenseService.insertAdsense(newAdsense);

        assertThat(adsenseInsertDto).isNotNull();
        assertThat(adsenseInsertDto.getSeller().getName()).isEqualTo("Mulher Maravilha");
        assertThat(adsenseInsertDto.getProduct().getName()).isEqualTo("Manteiga de Themysira");
    }

    @Test
    @DisplayName("CREATE: Valida se retorna a exceção BAD REQUEST quando tenta inserir um anúncio com ID.")
    void insertAdsense_returnThrow_whenInsertedAdsWithId() {
        Adsense adsenseWithId = AdsenseUtils.adsenseWithId();

        assertThrows(BadRequest.class, () -> adsenseService.insertAdsense(adsenseWithId));
    }

    @Test
    @DisplayName("UPDATE: Valida se retorna os dados do anúncio atualizado, quando o update é realizado com sucesso.")
    void updateAdsenseById_success() {
        Adsense adsense = AdsenseUtils.adsenseWithId();
        Long adsenseId = AdsenseUtils.adsenseWithId().getId();
        Long sellerId = SellerUtils.newSeller3ToSave().getId();

        BDDMockito.when(adsenseRepository.findById(adsenseId))
            .thenReturn(Optional.of(AdsenseUtils.adsenseWithId()));

        BDDMockito.when(productService.findById(anyLong()))
            .thenReturn(ProductUtils.newProduct5ToSave());

        AdsenseUpdateDto adsenseUpdatedDto = adsenseService.updateAdsenseById(adsense, adsenseId, sellerId);

        assertThat(adsenseUpdatedDto).isNotNull();
        assertThat(adsenseUpdatedDto.getPrice()).isPositive().isEqualTo(adsense.getPrice());
    }

    @Test
    @DisplayName("UPDATE: Valida se retorna a exception BAD REQUEST, quando o vendedor não é o proprietário do anúncio.")
    void updateAdsenseById_error() {
        Adsense adsense = AdsenseUtils.adsenseWithId();
        Long adsenseId = AdsenseUtils.adsenseWithId().getId();
        Long sellerId = SellerUtils.newSeller2ToSave().getId();

        BDDMockito.when(adsenseRepository.findById(adsenseId))
            .thenReturn(Optional.of(AdsenseUtils.adsenseWithId()));

        assertThrows(BadRequest.class, () -> adsenseService.updateAdsenseById(adsense, adsenseId, sellerId));
    }

    @Test
    void deleteAdsenseById() {
        Long adsenseId = AdsenseUtils.adsenseWithId().getId();

        BDDMockito.when(adsenseRepository.findById(adsenseId))
            .thenReturn(Optional.of(AdsenseUtils.adsenseWithId()));

        BDDMockito.doNothing()
            .when(adsenseRepository).deleteById(anyLong());

        assertThatCode(() -> {
           adsenseService.deleteAdsenseById(adsenseId);
        }).doesNotThrowAnyException();

        verify(adsenseRepository, atLeastOnce()).deleteById(anyLong());
    }

}

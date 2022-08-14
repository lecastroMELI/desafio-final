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
     */
    @GetMapping
    public ResponseEntity<List<AdsenseDto>> findAll() {
        return ResponseEntity.ok(AdsenseDto.convertListDto(adsenseService.findAll()));
    }

    /**
     * Nesse método retornamos anúncio listado por categoria
     * @param querytype
     */
    @GetMapping("/list")
    public ResponseEntity<List<AdsenseDto>> findByCategory(@RequestParam Category querytype) {
        return ResponseEntity.ok(AdsenseDto.convertListDto(adsenseService.findByCategory(querytype)));
    }

    /**
     * Nesse método retornamos um lista de anúncio por armazém / ou a qual armazém esse anúncio pertece
     * @param adsenseId
     */
    @GetMapping("/warehouse/{adsenseId}")
    public ResponseEntity <List<AdsenseByWarehouseDto>> getByAdsenseByWarehouse(@PathVariable Long adsenseId) {
        return ResponseEntity.status(HttpStatus.OK).body(adsenseService.findAdsenseByWarehouseAndQuantity(adsenseId));
    }

    /**
     * Método responsável pela criação de um novo anúncio. Retorna um ResponseEntity contendo o status da requisição e
     * no corpo os dados do anúncio criado.
     * @param newAdsense Os dados do anúncio informados na requisção
     * @return <ul>
     *     <li>ResponseEntity</li>
     *     <li>HttpStatus.CREATED</li>
     *     <li>Body</li>
     * </ul>
     * @author Letícia Castro
     */
    @PostMapping("/insert")
    public ResponseEntity<AdsenseInsertDto> createAdsense(@RequestBody Adsense newAdsense) {
        return ResponseEntity.status(HttpStatus.CREATED).body(adsenseService.insertAdsense(newAdsense));
    }

    /**
     * Método responsável por listar um anúncio específico. Retorna um ResponseEntity contendo o status da requisição e
     * no corpo os dados do anúncio se encontrado.
     * @param adsenseId O ID do anúncio a ser pesquisado no banco.
     * @return <ul>
     *     <li>ResponseEntity</li>
     *     <li>HttpStatus.OK</li>
     *     <li>Body</li>
     * </ul>
     * @author Letícia Castro
     */
    @GetMapping("/{adsenseId}")
    public ResponseEntity<AdsenseDto> readAdsenseById(@PathVariable Long adsenseId) {
        return ResponseEntity.status(HttpStatus.OK).body(AdsenseDto.convertDto(adsenseService.findById(adsenseId)));
    }

    /**
     * Método responsável por atualizar um anúncio específico de um determinado vendedor.
     * Retorna um ResponseEntity contendo o status da requisição
     * e no corpo os dados do anúncio atualizado.
     * @param adsenseId O ID do anúncio a ser pesquisado no banco.
     * @param sellerId O ID do vendedor.
     * @param adsense Os novos dados do anúncio.
     * @return <ul>
     *     <li>ResponseEntity</li>
     *     <li>HttpStatus.OK</li>
     *     <li>Body</li>
     * </ul>
     * @author Letícia Castro
     */
    @PutMapping("/{adsenseId}")
    public ResponseEntity<AdsenseUpdateDto> updateAdsense(@RequestBody Adsense adsense,
        @PathVariable Long adsenseId, @RequestParam Long sellerId) {

        return ResponseEntity.status(HttpStatus.OK)
            .body(adsenseService.updateAdsenseById(adsense, adsenseId, sellerId));
    }

    /**
     * Método responsável por apagar um anúncio específico.
     * @param adsenseId O ID do anúncio a ser pesquisado no banco.
     * @return HttpStatus.NO_CONTENT
     * @author Letícia Castro
     */
    @DeleteMapping("/{adsenseId}")
    public ResponseEntity<Void> deleteAdsense(@PathVariable Long adsenseId) {
        this.adsenseService.deleteAdsenseById(adsenseId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}


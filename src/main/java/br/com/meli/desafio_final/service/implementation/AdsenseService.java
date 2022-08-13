package br.com.meli.desafio_final.service.implementation;

import br.com.meli.desafio_final.dto.AdsenseByWarehouseDto;
import br.com.meli.desafio_final.dto.AdsenseInsertDto;
import br.com.meli.desafio_final.exception.BadRequest;
import br.com.meli.desafio_final.exception.NotFound;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.enums.Category;
import br.com.meli.desafio_final.repository.AdsenseRepository;
import br.com.meli.desafio_final.repository.ProductRepository;
import br.com.meli.desafio_final.repository.SellerRepository;
import br.com.meli.desafio_final.service.implementation.BatchService;
import br.com.meli.desafio_final.service.IAdsenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.meli.desafio_final.dto.AdsenseIdDto;

import javax.validation.constraints.Null;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdsenseService implements IAdsenseService {

    @Autowired
    private AdsenseRepository adsenseRepository;

    @Autowired
    private BatchService batchService;

    @Autowired
    private ProductService productService;

    /**
     * Nesse método estamos retornado/ consultando anúncio Id
     * @param id
     * @return
     */
    @Override
    public Adsense findById(long id) {
        return adsenseRepository.findById(id)
                .orElseThrow(() -> { throw new NotFound("Anúncio não cadastrado."); } );
    }

    /**
     * Nesse método consultamos uma lista de anúncios e retornamos lista caso existe, caso não exibimos uma mensagem de erro
     * @return
     */
    @Override
    public List<Adsense> findAll() {
        List<Adsense> adsenses = adsenseRepository.findAll();
        if (adsenses.size() == 0) throw new NotFound("💢 Lista de anúncios não encontrada");
        return adsenses;
    }

    /**
     * Nesse método retornamos uma lista de anúncio filtrado por categoria
     * @param category
     * @return
     */
    @Override
    public List<Adsense> findByCategory(Category category) {
        List<Adsense> adsenseList = findAll()
            .stream()
            .filter(a -> a.getProduct().getCategory().equals(category))
            .collect(Collectors.toList());

        if (adsenseList.isEmpty()) throw new NotFound("Não existem anuncios com essa categoria");
        return adsenseList;
    }
    // TODO: Test caminho triste

    /**
     * Nesse método retornamos uma lista de anúncio filtrado por produtos e Id
     * @param productId
     * @return
     */
    @Override
    public List<AdsenseIdDto> findByProductId(Long productId) {
        List<Adsense> adsenseList = findAll().stream().filter(a -> a.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
        return AdsenseIdDto.convertDto(adsenseList);
    }

    /**
     * Nesse método retornamos uma lista de anúncio e quantidade por armazém
     * @param adsenseId
     * @return
     */
    @Override
    public List<AdsenseByWarehouseDto> findAdsenseByWarehouseAndQuantity(Long adsenseId) {
        return batchService.getAdsenseByWarehouseAndQuantity(adsenseId);
    }

    /**
     * <ul>
     *     <li> Lista</li>
     * </ul>
     * @author Letícia Castro
     * @param newAdsense
     * @return
     */
    @Override
    public AdsenseInsertDto insertAdsense(Adsense newAdsense, Long sellerId) {
        if (newAdsense.getId() != null) {
            throw new BadRequest("⚠️ O anúncio não pode conter um ID.");
        }
        adsenseRepository.save(newAdsense);
        String productName = productService.findById(newAdsense.getProduct().getId()).getName();
        return new AdsenseInsertDto(newAdsense, productName);
    }

    @Override
    public void updateAdsenseById(Long id) {

    }

    @Override
    public void deleteAdsenseById(Long id) {

    }
}

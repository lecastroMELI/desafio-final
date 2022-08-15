package br.com.meli.desafio_final.service.implementation;

import br.com.meli.desafio_final.dto.AdsenseByWarehouseDto;
import br.com.meli.desafio_final.dto.AdsenseInsertDto;
import br.com.meli.desafio_final.dto.AdsenseUpdateDto;
import br.com.meli.desafio_final.exception.BadRequest;
import br.com.meli.desafio_final.exception.NotFound;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.entity.Product;
import br.com.meli.desafio_final.model.entity.Seller;
import br.com.meli.desafio_final.model.enums.Category;
import br.com.meli.desafio_final.repository.AdsenseRepository;
import br.com.meli.desafio_final.repository.SellerRepository;
import br.com.meli.desafio_final.service.IAdsenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.meli.desafio_final.dto.AdsenseIdDto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AdsenseService implements IAdsenseService {

    @Autowired
    private AdsenseRepository adsenseRepository;

    @Autowired
    private BatchService batchService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SellerRepository sellerRepository;

    /**
     * Nesse método estamos retornado/ consultando anúncio Id
     * @param id
     */
    @Override
    public Adsense findById(long id) {
        return adsenseRepository.findById(id)
            .orElseThrow(() -> { throw new NotFound("💢 Anúncio não cadastrado."); });
    }

    /**
     * Nesse método consultamos uma lista de anúncios e retornamos lista caso existe,
     * caso não exibimos uma mensagem de erro
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
     */
    @Override
    public List<Adsense> findByCategory(Category category) {
        List<Adsense> adsenseList = findAll()
            .stream()
            .filter(a -> a.getProduct().getCategory().equals(category))
            .collect(Collectors.toList());

        if (adsenseList.isEmpty()) throw new NotFound("💢 Não existem anúncios com essa categoria");

        return adsenseList;
    }
    // TODO: Test caminho triste

    /**
     * Nesse método retornamos uma lista de anúncio filtrado por produtos e Id
     * @param productId
     */
    @Override
    public List<AdsenseIdDto> findByProductId(Long productId) {
        List<Adsense> adsenseList = findAll()
            .stream()
            .filter(a -> a.getProduct().getId().equals(productId))
            .collect(Collectors.toList());

        return AdsenseIdDto.convertDto(adsenseList);
    }

    /**
     * Nesse método retornamos uma lista de anúncio e quantidade por armazém
     * @param adsenseId
     */
    @Override
    public List<AdsenseByWarehouseDto> findAdsenseByWarehouseAndQuantity(Long adsenseId) {
        return batchService.getAdsenseByWarehouseAndQuantity(adsenseId);
    }

    /**
     * Método responsável pela criação de um novo anúncio.
     * Retorna alguns dados do anúncio criado.
     * @param newAdsense Os dados do anúncio informados na requisção
     * @return AdsenseInsertDto
     * @author Letícia Castro
     */
    @Override
    public AdsenseInsertDto insertAdsense(Adsense newAdsense) {
        if (newAdsense.getId() != null) {
            throw new BadRequest("⚠️ O anúncio não pode conter um ID.");
        }

        Product product = productService.findById(newAdsense.getProduct().getId());

        Seller seller = sellerRepository
            .findById(newAdsense.getSeller().getId())
            .orElseThrow(() -> { throw new NotFound("Seller não cadastrado"); });

        adsenseRepository.save(newAdsense);

        return new AdsenseInsertDto(newAdsense, product, seller);
    }

    /**
     * Método responsável por atualizar um anúncio específico de um determinado vendedor.
     * Retorna alguns dados do anúncio atualizado.
     * @param adsenseId O ID do anúncio a ser pesquisado no banco.
     * @param sellerId O ID do vendedor.
     * @param adsense Os novos dados do anúncio.
     * @return AdsenseUpdateDto
     * @author Letícia Castro
     */
    @Override
    public AdsenseUpdateDto updateAdsenseById(Adsense adsense, Long adsenseId, Long sellerId) {
        Adsense adsenseFound = findById(adsenseId);

        if (!sellerId.equals(adsenseFound.getSeller().getId())) {
            throw new BadRequest("Vendendor não é o proprietário deste anúncio.");
        }

        Product product = productService.findById(adsense.getProduct().getId());

        adsenseFound.setProduct(adsense.getProduct());
        adsenseFound.setPrice(adsense.getPrice());

        adsenseRepository.save(adsenseFound);

        return new AdsenseUpdateDto(adsenseFound, product);
    }

    /**
     * Método responsável por apagar um anúncio específico.
     * @param id - ID do anúncio
     * @author Letícia Castro
     */
    @Override
    public void deleteAdsenseById(Long id) {
        Adsense adsense = findById(id);
        if (Objects.nonNull(adsense)) {
            adsenseRepository.deleteById(id);
        }
    }
}

// TODO : ADSENSE: MELHORIAS: UPDATE SÓ É PERMITIDO SE NÃO HOUVE NENHUMA VENDA
// TODO : ADSENSE: MELHORIAS: DELETE SÓ SER PERMITIDO SE O SELLER FOR O PROPRIETÁRIO
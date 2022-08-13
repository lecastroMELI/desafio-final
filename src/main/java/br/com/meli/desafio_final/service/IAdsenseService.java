package br.com.meli.desafio_final.service;
import br.com.meli.desafio_final.dto.*;
import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.enums.Category;

import java.util.List;

public interface IAdsenseService {

    Adsense findById(long id);
    List<Adsense> findAll();
    List<Adsense> findByCategory(Category category);
    List<AdsenseByWarehouseDto> findAdsenseByWarehouseAndQuantity(Long adsenseId);
    List<AdsenseIdDto> findByProductId(Long productId);

    AdsenseInsertDto insertAdsense(Adsense newAdsense);
    AdsenseUpdateDto updateAdsenseById(Adsense adsense, Long adsenseId, Long sellerId);
    void deleteAdsenseById(Long id);
}

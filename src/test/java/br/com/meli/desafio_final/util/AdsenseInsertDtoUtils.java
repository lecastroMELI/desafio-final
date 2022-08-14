package br.com.meli.desafio_final.util;

import br.com.meli.desafio_final.dto.AdsenseInsertDto;
import br.com.meli.desafio_final.model.entity.Adsense;

public class AdsenseInsertDtoUtils {

    public static Adsense newAdsenseInsertToSave() {
        return Adsense.builder()
            .price(19.19)
            .seller(SellerUtils.newSeller3ToSave())
            .product(ProductUtils.newProduct5ToSave())
            .build();
    }

    public static AdsenseInsertDto newAdsenseInsertDtoToSave() {
        return AdsenseInsertDto.builder()
            .adsenseId(1L)
            .price(19.19)
            .seller(SellerUtils.newSeller1ToSave())
            .product(ProductUtils.newProduct1ToSave())
            .build();
    }

}

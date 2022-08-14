package br.com.meli.desafio_final.util;

import br.com.meli.desafio_final.dto.AdsenseUpdateDto;

public class AdsenseUpdateDtoUtils {

    public static AdsenseUpdateDto adsenseUpdated() {
        return AdsenseUpdateDto.builder()
            .adsenseId(AdsenseUtils.adsenseWithId().getId())
            .price(AdsenseUtils.adsenseWithId().getPrice())
            .product(ProductUtils.newProduct5ToSave())
            .build();
    }
}

package br.com.meli.desafio_final.dto;

import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.entity.Product;
import br.com.meli.desafio_final.model.entity.Seller;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdsenseUpdateDto {

    private Long adsenseId;
    private Double price;

    private Product product;

    public AdsenseUpdateDto(Adsense adsense, Product prod) {
        setAdsenseId(adsense.getId());
        setPrice(adsense.getPrice());
        setProduct(prod);
    }

}

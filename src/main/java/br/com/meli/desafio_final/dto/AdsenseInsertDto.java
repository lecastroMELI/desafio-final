package br.com.meli.desafio_final.dto;

import br.com.meli.desafio_final.model.entity.Adsense;
import br.com.meli.desafio_final.model.entity.Product;
import br.com.meli.desafio_final.model.entity.Seller;
import br.com.meli.desafio_final.repository.AdsenseRepository;
import br.com.meli.desafio_final.repository.ProductRepository;
import br.com.meli.desafio_final.repository.SellerRepository;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdsenseInsertDto {

    private Long adsenseId;
    private Double price;

    private Seller seller;
    private Product product;

    public AdsenseInsertDto(Adsense adsense, Product prod, Seller sell) {
        setAdsenseId(adsense.getId());
        setPrice(adsense.getPrice());
        setProduct(prod);
        setSeller(sell);
    }

}

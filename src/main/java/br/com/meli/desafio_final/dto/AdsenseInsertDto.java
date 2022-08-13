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
    private Long sellerId;
    // private String sellerName;
    private Long productId;
    private String productName;

    public AdsenseInsertDto(Adsense adsense, String prodName) {
        setAdsenseId(adsense.getId());
        setPrice(adsense.getPrice());
        setSellerId(adsense.getSeller().getId());
        // setSellerName(adsense.getSeller().getName());
        setProductId(adsense.getProduct().getId());
        setProductName(prodName);
    }

}

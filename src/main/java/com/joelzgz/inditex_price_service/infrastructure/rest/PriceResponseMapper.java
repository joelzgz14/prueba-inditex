package com.joelzgz.inditex_price_service.infrastructure.rest;

import com.joelzgz.inditex_price_service.domain.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceResponseMapper {

    public PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPrice(),
                price.getCurrency()
        );
    }
}
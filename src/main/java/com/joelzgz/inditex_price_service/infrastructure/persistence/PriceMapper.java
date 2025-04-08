package com.joelzgz.inditex_price_service.infrastructure.persistence;

import com.joelzgz.inditex_price_service.domain.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceMapper {

    public Price toDomain(PriceEntity entity) {
        Price domain = new Price();
        domain.setId(entity.getId());
        domain.setBrandId(entity.getBrandId());
        domain.setStartDate(entity.getStartDate());
        domain.setEndDate(entity.getEndDate());
        domain.setPriceList(entity.getPriceList());
        domain.setProductId(entity.getProductId());
        domain.setPriority(entity.getPriority());
        domain.setPrice(entity.getPrice());
        domain.setCurrency(entity.getCurrency());
        return domain;
    }

    public PriceEntity toEntity(Price domain) {
        PriceEntity entity = new PriceEntity();
        entity.setId(domain.getId());
        entity.setBrandId(domain.getBrandId());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setPriceList(domain.getPriceList());
        entity.setProductId(domain.getProductId());
        entity.setPriority(domain.getPriority());
        entity.setPrice(domain.getPrice());
        entity.setCurrency(domain.getCurrency());
        return entity;
    }
}
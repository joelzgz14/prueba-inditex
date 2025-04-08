package com.joelzgz.inditex_price_service.domain.port.out;

import com.joelzgz.inditex_price_service.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {
    List<Price> findByProductIdBrandIdAndDate(Long productId, Integer brandId, LocalDateTime date);
}

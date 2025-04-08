package com.joelzgz.inditex_price_service.domain.port.in;

import com.joelzgz.inditex_price_service.domain.model.Price;

import java.time.LocalDateTime;

public interface GetPriceUseCase {
    Price getApplicablePrice(Long productId, Integer brandId, LocalDateTime applicationDate);
}
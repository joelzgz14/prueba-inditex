package com.joelzgz.inditex_price_service.application.service;

import com.joelzgz.inditex_price_service.domain.exception.PriceNotFoundException;
import com.joelzgz.inditex_price_service.domain.model.Price;
import com.joelzgz.inditex_price_service.domain.port.in.GetPriceUseCase;

// application/service/PriceService.java

import com.joelzgz.inditex_price_service.domain.port.out.PriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class PriceService implements GetPriceUseCase {

    private final PriceRepository priceRepository;

    public PriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    @Override
    public Price getApplicablePrice(Long productId, Integer brandId, LocalDateTime applicationDate) {
        List<Price> prices = priceRepository.findByProductIdBrandIdAndDate(productId, brandId, applicationDate);

        if (prices.isEmpty()) {
            throw new PriceNotFoundException("No price found for productId=" + productId +
                    ", brandId=" + brandId +
                    ", date=" + applicationDate);
        }

        // Ordenar por prioridad descendente y tomar el primero
        return prices.stream()
                .max(Comparator.comparing(Price::getPriority))
                .orElseThrow(() -> new PriceNotFoundException("Error retrieving price"));
    }
}

package com.joelzgz.inditex_price_service.infrastructure.persistence;

import com.joelzgz.inditex_price_service.domain.model.Price;
import com.joelzgz.inditex_price_service.domain.port.out.PriceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PriceRepositoryAdapter implements PriceRepository {

    private final PriceJpaRepository priceJpaRepository;
    private final PriceMapper priceMapper;

    public PriceRepositoryAdapter(PriceJpaRepository priceJpaRepository, PriceMapper priceMapper) {
        this.priceJpaRepository = priceJpaRepository;
        this.priceMapper = priceMapper;
    }

    @Override
    public List<Price> findByProductIdBrandIdAndDate(Long productId, Integer brandId, LocalDateTime date) {
        List<PriceEntity> priceEntities = priceJpaRepository.findByProductIdBrandIdAndDate(productId, brandId, date);
        return priceEntities.stream()
                .map(priceMapper::toDomain)
                .collect(Collectors.toList());
    }
}
package com.joelzgz.inditex_price_service.infrastructure.persistence;


import com.joelzgz.inditex_price_service.domain.model.Price;
import com.joelzgz.inditex_price_service.domain.port.out.PriceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PriceJpaRepository extends JpaRepository<PriceEntity, Long> {

    @Query("SELECT p FROM PriceEntity p WHERE p.productId = :productId " +
            "AND p.brandId = :brandId " +
            "AND :date BETWEEN p.startDate AND p.endDate")
    List<PriceEntity> findByProductIdBrandIdAndDate(
            @Param("productId") Long productId,
            @Param("brandId") Integer brandId,
            @Param("date") LocalDateTime date);
}


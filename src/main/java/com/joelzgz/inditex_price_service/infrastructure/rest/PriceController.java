package com.joelzgz.inditex_price_service.infrastructure.rest;

import com.joelzgz.inditex_price_service.domain.model.Price;
import com.joelzgz.inditex_price_service.domain.port.in.GetPriceUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
public class PriceController {

    private final GetPriceUseCase getPriceUseCase;
    private final PriceResponseMapper responseMapper;

    public PriceController(GetPriceUseCase getPriceUseCase, PriceResponseMapper responseMapper) {
        this.getPriceUseCase = getPriceUseCase;
        this.responseMapper = responseMapper;
    }

    @GetMapping
    public ResponseEntity<PriceResponse> getPrice(
            @RequestParam Long productId,
            @RequestParam Integer brandId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate) {

        Price price = getPriceUseCase.getApplicablePrice(productId, brandId, applicationDate);
        PriceResponse response = responseMapper.toResponse(price);

        return ResponseEntity.ok(response);
    }
}
package com.nbp.currencyconverter.controller;

import com.nbp.currencyconverter.model.ExchangeRate;
import com.nbp.currencyconverter.model.UserValue;
import com.nbp.currencyconverter.service.ConverterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class ConverterController {

    private final ConverterService converterService;

    public ConverterController(ConverterService converterService) {
        this.converterService = converterService;
    }

    @GetMapping("/exchange-rate")
    public ExchangeRate getExchangeRates() {
        return converterService.getExchangeRates();
    }

    @PostMapping("/process-value")
    public ResponseEntity<BigDecimal> processValue(@RequestBody UserValue userValue) {
        return converterService.processValue(userValue);
    }

    @GetMapping("/mid-value")
    public BigDecimal getGBPMidValue() {
        return converterService.getGBPMidValue();
    }

}

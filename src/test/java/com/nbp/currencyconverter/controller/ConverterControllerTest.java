package com.nbp.currencyconverter.controller;

import com.nbp.currencyconverter.model.ExchangeRate;
import com.nbp.currencyconverter.model.UserValue;
import com.nbp.currencyconverter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ConverterControllerTest {

    @Mock
    ConverterService converterService;

    ConverterController converterController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        converterController = new ConverterController(converterService);
    }

    @Test
    void getExchangeRates_ShouldReturnExchangeRate() {
        ExchangeRate exchangeRate = new ExchangeRate();
        when(converterService.getExchangeRates()).thenReturn(exchangeRate);

        assertEquals(exchangeRate, converterController.getExchangeRates());
        verify(converterService, times(1)).getExchangeRates();
    }

    @Test
    void convertCurrency_ShouldReturnResponseEntityWithBigDecimal() {
        UserValue userValue = new UserValue();
        BigDecimal convertedValue = BigDecimal.valueOf(10);
        when(converterService.convertCurrency(userValue)).thenReturn(ResponseEntity.ok(convertedValue));

        ResponseEntity<BigDecimal> responseEntity = converterController.convertCurrency(userValue);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(convertedValue, responseEntity.getBody());
        verify(converterService, times(1)).convertCurrency(userValue);
    }

    @Test
    void getGBPMidValue_ShouldReturnBigDecimal() {
        BigDecimal midValue = BigDecimal.valueOf(10);
        when(converterService.getGBPMidValue()).thenReturn(midValue);

        assertEquals(midValue, converterController.getGBPMidValue());
        verify(converterService, times(1)).getGBPMidValue();
    }
}

package com.nbp.currencyconverter.service;

import com.nbp.currencyconverter.model.ExchangeRate;
import com.nbp.currencyconverter.model.Rate;
import com.nbp.currencyconverter.model.UserValue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConverterServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ConverterService converterService;

    private static final String API_URL = "https://api.nbp.pl/api/exchangerates/rates/A/GBP";

    @Before
    public void setUp() {
        converterService = new ConverterService(restTemplate);
    }

    @Test
    public void getExchangeRates_returnsExchangeRate() {
        ExchangeRate expectedExchangeRate = new ExchangeRate();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal("5.0"));
        expectedExchangeRate.setRates(List.of(rate));

        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(expectedExchangeRate, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        ExchangeRate actualExchangeRate = converterService.getExchangeRates();

        assertEquals(expectedExchangeRate, actualExchangeRate);
    }

    @Test
    public void getGBPMidValue_returnsMidValue() {
        ExchangeRate exchangeRate = new ExchangeRate();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal("5.0"));
        exchangeRate.setRates(List.of(rate));

        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(exchangeRate, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        BigDecimal expectedMidValue = new BigDecimal("5.00");
        BigDecimal actualMidValue = converterService.getGBPMidValue();

        assertEquals(expectedMidValue, actualMidValue);
    }

    @Test(expected = RuntimeException.class)
    public void getGBPMidValue_throwsRuntimeExceptionWhenExchangeRateIsNull() {
        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        converterService.getGBPMidValue();
    }

    @Test
    public void convertCurrency_returnsConvertedValue() {
        UserValue userValue = new UserValue();
        userValue.setRateName("PLN");
        userValue.setValue("10.0");

        ExchangeRate exchangeRate = new ExchangeRate();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal("5.0"));
        exchangeRate.setRates(List.of(rate));

        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(exchangeRate, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        ResponseEntity<BigDecimal> responseEntityExpected = ResponseEntity.ok(new BigDecimal("50.00"));
        ResponseEntity<BigDecimal> responseEntityActual = converterService.convertCurrency(userValue);

        assertEquals(responseEntityExpected, responseEntityActual);
    }

    @Test
    public void convertCurrency_returnsZeroWhenInputValueIsEmpty() {
        UserValue userValue = new UserValue();
        userValue.setRateName("PLN");
        userValue.setValue("");

        ExchangeRate exchangeRate = new ExchangeRate();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal("5.0"));
        exchangeRate.setRates(List.of(rate));

        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(exchangeRate, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        ResponseEntity<BigDecimal> responseEntityExpected = ResponseEntity.ok(BigDecimal.ZERO.setScale(2));
        ResponseEntity<BigDecimal> responseEntityActual = converterService.convertCurrency(userValue);

        assertEquals(responseEntityExpected, responseEntityActual);
    }

    @Test
    public void convertCurrency_returnsZeroWhenRateNameIsNotValid() {
        UserValue userValue = new UserValue();
        userValue.setRateName("XYZ");
        userValue.setValue("10.0");

        ExchangeRate exchangeRate = new ExchangeRate();
        Rate rate = new Rate();
        rate.setMid(new BigDecimal("5.0"));
        exchangeRate.setRates(List.of(rate));

        ResponseEntity<ExchangeRate> responseEntity = new ResponseEntity<>(exchangeRate, HttpStatus.OK);

        when(restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class)).thenReturn(responseEntity);

        ResponseEntity<BigDecimal> responseEntityExpected = ResponseEntity.ok(BigDecimal.ZERO.setScale(2));
        ResponseEntity<BigDecimal> responseEntityActual = converterService.convertCurrency(userValue);

        assertEquals(responseEntityExpected, responseEntityActual);
    }
}
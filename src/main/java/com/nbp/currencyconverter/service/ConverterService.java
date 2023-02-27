package com.nbp.currencyconverter.service;

import com.nbp.currencyconverter.model.ExchangeRate;
import com.nbp.currencyconverter.model.UserValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ConverterService {

    private final RestTemplate restTemplate;

    @Autowired
    public ConverterService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static final String API_URL = "https://api.nbp.pl/api/exchangerates/rates/A/GBP";

    public ExchangeRate getExchangeRates() {
        ResponseEntity<ExchangeRate> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class);
        return response.getBody();
    }

    public BigDecimal getGBPMidValue() {

        ResponseEntity<ExchangeRate> response = restTemplate.exchange(API_URL, HttpMethod.GET, null, ExchangeRate.class);

        ExchangeRate exchangeRate = response.getBody();

        if (exchangeRate != null) {
            return exchangeRate.getRates().get(0).getMid().setScale(2, RoundingMode.DOWN);
        } else {
            throw new RuntimeException("Failed to retrieve exchange rate from API");
        }

    }

    public ResponseEntity<BigDecimal> convertCurrency(UserValue userValue) {

        String inputValue = userValue.getValue().trim();
        String rateName = userValue.getRateName();

        BigDecimal mid = getGBPMidValue();

        BigDecimal valueFromInput;

        if (inputValue.isEmpty()) {
            valueFromInput = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            valueFromInput = new BigDecimal(inputValue).setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal result = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        if ("PLN".equals(rateName)) {
            result = valueFromInput.multiply(mid).setScale(2, RoundingMode.HALF_UP);
        }
        if ("GBP".equals(rateName)) {
            result = valueFromInput.divide(mid, 2, RoundingMode.HALF_UP);
        }

        return ResponseEntity.ok(result);

    }

}

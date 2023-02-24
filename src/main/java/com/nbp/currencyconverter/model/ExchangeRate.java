package com.nbp.currencyconverter.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExchangeRate {

    private String currency;
    private String code;
    private List<Rate> rates;

}

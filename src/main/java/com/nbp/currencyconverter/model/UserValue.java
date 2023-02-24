package com.nbp.currencyconverter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserValue {

    private String value;

    @JsonProperty("name")
    private String rateName;

}

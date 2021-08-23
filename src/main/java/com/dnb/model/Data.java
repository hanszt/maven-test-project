package com.dnb.model;

import java.math.BigDecimal;

public class Data {

    private final String id;
    private final BigDecimal amount;

    public Data(String id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }
    public BigDecimal getAmount() {
        return amount;
    }

}

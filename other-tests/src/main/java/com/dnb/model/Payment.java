package com.dnb.model;

import java.math.BigDecimal;

public class Payment {

    private final String id;
    private final BigDecimal amount;

    public Payment(String id, BigDecimal amount) {
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

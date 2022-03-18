package org.hzt.model;

import java.math.BigDecimal;
import java.util.Objects;

public final class Payment {
    private final String id;
    private final BigDecimal amount;

    public Payment(String id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public String id() {
        return id;
    }

    public BigDecimal amount() {
        return amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (Payment) obj;
        return Objects.equals(this.id, that.id) &&
               Objects.equals(this.amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, amount);
    }

    @Override
    public String toString() {
        return "Payment[" +
               "id=" + id + ", " +
               "amount=" + amount + ']';
    }

}

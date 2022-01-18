package org.hzt.collectors_samples;

import java.lang.reflect.Field;

public class CashBalance {

    private final String bank;
    private final boolean isOpening;

    public CashBalance(String bank, boolean isOpening) {
        this.bank = bank;
        this.isOpening = isOpening;
    }

    public String getBank() {
        return bank;
    }

    public boolean isOpening() {
        return isOpening;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        Class<?> curr = this.getClass();
        sb.append(curr.getSimpleName()).append("{");
        while (curr != null) {
            Field[] field = curr.getDeclaredFields();
            for (Field value : field) {
                try {
                    sb.append(value.getName()).append("='").append(value.get(this).toString()).append("', ");
                } catch (IllegalAccessException e) {
                    throw new IllegalArgumentException(e);
                }
            }
            curr = curr.getSuperclass();
        }
        sb.append("}");
        return sb.toString();
    }

}

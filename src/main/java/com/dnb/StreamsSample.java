package com.dnb;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.System.*;

public class StreamsSample {

    public static void main(String[] args) {
        List<Data> list = List.of(
                new Data("1", BigDecimal.valueOf(1000000)),
                new Data("2", BigDecimal.valueOf(4000000)),
                new Data("3", BigDecimal.valueOf(2000000)));
        BigDecimal sum = list.stream().map(Data::getAmount).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
        out.println(sum);
    }


    private static class Data {

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
}

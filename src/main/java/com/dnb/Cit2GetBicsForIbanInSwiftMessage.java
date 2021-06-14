package com.dnb;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Cit2GetBicsForIbanInSwiftMessage {

    private Cit2GetBicsForIbanInSwiftMessage() {
    }

    public static Set<Bic> getBicsForIbanInSwiftMessage(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Bic::new)
                .collect(Collectors.toSet());
    }
}

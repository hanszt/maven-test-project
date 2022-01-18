package org.hzt;

import org.hzt.model.Bic;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.*;

public final class Cit2GetBicsForIbanInSwiftMessage {

    private Cit2GetBicsForIbanInSwiftMessage() {
    }

    public static Set<Bic> getBicsForIbanInSwiftMessage(String input) {
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .map(Bic::new)
                .collect(toUnmodifiableSet());
    }
}

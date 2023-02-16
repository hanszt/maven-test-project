package org.hzt.interfaces;

import org.hzt.model.Bic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static org.hzt.utils.function.predicates.StringPredicates.contains;

public class AB implements A {

    private static final Logger LOGGER = LoggerFactory.getLogger(AB.class);

    public static void main(String... args) {
        Bic bic = new Bic("");
        LOGGER.info("{}", bic);
        List<String> strings = new ArrayList<>(List.of("Hallo" ,"hoe", "gaat", "het?"));
        strings.removeIf(contains("h"));
        LOGGER.info("strings = {}", strings);
        new AB().test();
    }

    private void test() {
        LOGGER.info("{}", size());
    }
}

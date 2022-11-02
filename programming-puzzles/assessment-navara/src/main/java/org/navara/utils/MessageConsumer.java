package org.navara.utils;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class MessageConsumer {

    private final Consumer<String> stringConsumer;
    private final Consumer<Supplier<String>> stringSupplierConsumer;

    public MessageConsumer(final Consumer<String> stringConsumer) {
        this.stringConsumer = stringConsumer;
        this.stringSupplierConsumer = stringSupplier -> stringConsumer.accept(stringSupplier.get());
    }

    public void accept(final String string) {
        stringConsumer.accept(string);
    }

    public void accept(final Supplier<String> stringSupplier) {
        stringSupplierConsumer.accept(stringSupplier);
    }

}

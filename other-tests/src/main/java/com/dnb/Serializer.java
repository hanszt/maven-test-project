package com.dnb;

import org.hzt.model.Book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Serializer {

    private static final String TEST_FILE_NAME = "serialization_test";

    private Serializer() {
    }

    public static void serialize(Book book) throws IOException {
        try (var file = new FileOutputStream(TEST_FILE_NAME)) {
            var out = new ObjectOutputStream(file);
            out.writeObject(book);
            out.close();
        }
    }

    public static Book deserialize() {
        try (var file = new FileInputStream(TEST_FILE_NAME);
              var in = new ObjectInputStream(file)) {
            return (Book) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}

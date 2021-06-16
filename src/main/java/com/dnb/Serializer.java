package com.dnb;

import com.dnb.model.Book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {

    private Serializer() {
    }

    private static final String TEST_FILE_NAME = "serialization_test";

    public static void serialize(Book book) throws IOException {
        try (var file = new FileOutputStream(TEST_FILE_NAME)) {
            var out = new ObjectOutputStream(file);
            out.writeObject(book);
            out.close();
        }
    }

    public static Book deserialize() throws IOException, ClassNotFoundException {
        try (var file = new FileInputStream(TEST_FILE_NAME);
              var in = new ObjectInputStream(file)) {
            return (Book) in.readObject();
        }


    }
}

package com.dnb;

import org.hzt.model.Book;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class Serializer {

    private Serializer() {
    }

    public static void serialize(Book book, String destinationFileName) throws IOException {
        try (var fileOutputStream = new FileOutputStream(destinationFileName);
             var out = new ObjectOutputStream(fileOutputStream)) {
            out.writeObject(book);
        }
    }

    public static Book deserialize(String fileName) {
        try (var fileInputStream = new FileInputStream(fileName);
             var objectInputStream = new ObjectInputStream(fileInputStream)) {
            return (Book) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}

package com.dnb;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

public final class Serializer {

    private Serializer() {
    }

    public static <T extends Serializable> void serialize(T value, String destinationFileName) throws IOException {
        try (var fileOutputStream = new FileOutputStream(destinationFileName);
             var out = new ObjectOutputStream(fileOutputStream)) {
            out.writeObject(value);
        }
    }

    public static <T> Optional<T> deserialize(String fileName, Class<T> tClass) {
        try (var fileInputStream = new FileInputStream(fileName);
             var objectInputStream = new ObjectInputStream(fileInputStream)) {
            return Optional.of(objectInputStream.readObject())
                    .filter(tClass::isInstance)
                    .map(tClass::cast);
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}

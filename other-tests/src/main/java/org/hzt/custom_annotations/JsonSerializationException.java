package org.hzt.custom_annotations;

public class JsonSerializationException extends RuntimeException {

    public JsonSerializationException(String message) {
        super(message);
    }

    public JsonSerializationException(Throwable cause) {
        super(cause);
    }
}

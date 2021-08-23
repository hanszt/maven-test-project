package com.dnb.custom_annotations;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Hans Zuidervaart
 * To gain more insight in the workings of annotations.
 * @see <a href="https://www.baeldung.com/java-custom-annotation">Creating a Custom Annotation in Java</a>
 *
 * Annotations is a new feature from Java 5.
 * Annotations are a kind of comment or meta data you can insert in your Java code.
 * These annotations can then be processed at compile time by pre-compiler tools, or at runtime via Java Reflection.
 * @see <a href="http://tutorials.jenkov.com/java-reflection/annotations.html">Java Reflection - Annotations</a>
 */
public class ObjectToJsonConverter {

    private static boolean isAnnotatedAsJsonElement(Field field) {
        field.setAccessible(true);
        return field.isAnnotationPresent(JsonElement.class);
    }

    private static void checkIfSerializable(Object object) {
        if (Objects.isNull(object)) {
            throw new JsonSerializationException("The object to serialize is null");
        }
        Class<?> theClass = object.getClass();
        if (!theClass.isAnnotationPresent(JsonSerializable.class)) {
            throw new JsonSerializationException("The class " + theClass.getSimpleName() + " is not annotated with JsonSerializable");
        }
    }

    private static void initializeObject(Object object) throws InvocationTargetException, IllegalAccessException {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Initializable.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }

    private static String getJsonString(Object object) {
        String jsonString = Arrays.stream(object.getClass().getDeclaredFields())
                .filter(ObjectToJsonConverter::isAnnotatedAsJsonElement)
                .map(field -> toKeyValuePair(object, field))
                .map(entry -> String.format("\"%s\":\"%s\"", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(","));
        return "{" + jsonString + "}";
    }

    private static SimpleEntry<String, String> toKeyValuePair(Object object, Field field) {
        final var key = field.getAnnotation(JsonElement.class).key();
        try {
            return new SimpleEntry<>(key.isEmpty() ? field.getName() : key, (String) field.get(object));
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public String convertToJson(Object object) {
        try {
            checkIfSerializable(object);
            initializeObject(object);
            return getJsonString(object);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new JsonSerializationException(e);
        }
    }
}

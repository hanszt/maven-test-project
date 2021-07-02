package com.dnb;

import com.dnb.model.Bic;

import java.lang.reflect.Field;

public final class ReflectionSample {

    private ReflectionSample() {
    }

    static void setFinalFieldNameToNewValue(Bic bic, String newName) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = bic.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(bic, newName);
    }
}

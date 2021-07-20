package com.dnb;

import com.dnb.model.Bic;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Function;
import org.apache.commons.beanutils.PropertyUtils;

public final class ReflectionSample {

    private ReflectionSample() {
    }

    static void setFinalFieldNameToNewValue(Bic bic, String newName) throws NoSuchFieldException, IllegalAccessException {
        Field nameField = bic.getClass().getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(bic, newName);
    }

    /**
     * @author Job de Noo
     */
    public static class PicassoFunctions {

        private PicassoFunctions() {
            //hide implicit public constructor
        }

        public static <R> Function<R, String> stringPropertyAccessor(final String name) {
            return PicassoFunctions.propertyAccessorFunction(name);
        }

        public static <R> Function<R, Integer>integerPropertyAccessor(final String name) {
            return PicassoFunctions.propertyAccessorFunction(name);
        }


        public static <R> Function<R, LocalDate>localDatePropertyAccessor(final String name) {
            return PicassoFunctions.propertyAccessorFunction(name);
        }

        /**
         * Function that tries to return the given property path
         */
        public static <T, R> Function<R, T> propertyAccessorFunction(final String name) {
            return new Function<R, T>() {
                @Override
                public T apply(R input) {
                    try {
                        return (T) PropertyUtils.getProperty(input, name);
                    } catch (ReflectiveOperationException e) {
                        throw new IllegalStateException("error accessing property " + name + "on " + input, e);
                    }
                }
            };

        }

    }

}

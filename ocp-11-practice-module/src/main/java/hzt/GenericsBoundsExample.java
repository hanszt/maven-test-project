package hzt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class GenericsBoundsExample {

    public static void main(String[] args) {
        List<? super IOException> exceptions;

        exceptions = new ArrayList<>();

        exceptions.add(new FileNotFoundException());
        exceptions.forEach(System.out::println);

        annotationListExample();
    }

    private static void annotationListExample() {
        Deprecated deprecated = new Deprecated() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String since() {
                return null;
            }

            @Override
            public boolean forRemoval() {
                return false;
            }
        };

        Override override = new Override() {

            @Override
            public Class<? extends Annotation> annotationType() {
                return null;
            }

            @Override
            public String toString() {
                return "This is an Override instance";
            }
        };

        List<Annotation> annotations = new ArrayList<>();
        annotations.add(override);
        annotations.add(deprecated);
        System.out.println("Annotations: ");
        annotations.forEach(System.out::println);
    }
}

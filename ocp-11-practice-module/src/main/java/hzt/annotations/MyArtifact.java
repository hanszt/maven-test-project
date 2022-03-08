package hzt.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME) //if this is omitted, the annotation will not be visible during runtime
public @interface MyArtifact {
    int id() default 0;

    String name();
}

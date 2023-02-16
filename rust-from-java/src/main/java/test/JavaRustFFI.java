package test;

import java.io.File;
import java.util.Arrays;

@SuppressWarnings(JavaRustFFI.REPLACE_SYSTEM_OUT_BY_LOGGER)
public class JavaRustFFI {

    private static final String NO_SNAKE_CASE_IN_JAVA = "java:S100";
    static final String REPLACE_SYSTEM_OUT_BY_LOGGER = "java:S106";

    public static native void hello();

    @SuppressWarnings(NO_SNAKE_CASE_IN_JAVA)
    public static native byte[] optimize_from_memory(byte[] data);

    public static void main(String... args) {
        System.load(new File("rust-from-java/optimize/target/debug/optimize.dll").getAbsolutePath());

        hello();

        byte[] bytes = JavaRustFFI.optimize_from_memory(new byte[]{1, 2, 3, 4});

        System.out.println(Arrays.toString(bytes));
    }
}

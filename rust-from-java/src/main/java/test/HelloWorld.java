package test;

import java.io.File;

/**
 * @see <a href="https://docs.rs/jni/latest/jni/">Safe JNI Bindings in Rust</a>
 */
@SuppressWarnings(JavaRustFFI.REPLACE_SYSTEM_OUT_BY_LOGGER)
public class HelloWorld {

    // This declares that the static `hello` method will be provided as a native library.
    private static native String hello(String input);

    static {
        System.load(new File("rust-from-java/mylib/target/debug/mylib.dll").getAbsolutePath());
    }

    public static void main(String[] args) {
        String output = HelloWorld.hello("Hans");
        System.out.println(output);
    }
}

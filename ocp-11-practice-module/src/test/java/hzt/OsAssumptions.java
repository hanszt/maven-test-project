package hzt;

import java.util.Locale;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * A class to make assumptions about the operating system that is being worked with
 */
public final class OsAssumptions {

    private OsAssumptions() {
    }

    public static void assumeIsWindowsOs() {
        String osName = System.getProperty("os.name");
        assumeTrue(osName.toLowerCase(Locale.ROOT).contains("windows"), "Os is not windows. (Was `" + osName + "')");
    }
}

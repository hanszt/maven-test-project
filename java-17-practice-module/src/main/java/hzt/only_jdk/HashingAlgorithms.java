package hzt.only_jdk;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashingAlgorithms {

    private HashingAlgorithms() {
    }

    /**
     * <a href="https://www.baeldung.com/sha-256-hashing-java">SHA-256 and SHA3-256 Hashing in Java</a>
     *
     * @param originalString The string to calculate to hash
     * @param algorithm the algorithm used
     * @return The hash
     */
    public static String toHexadecimalHash(String originalString, Algorithm algorithm) {
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm.asString);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        byte[] encodedHash = digest.digest(originalString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedHash);
    }

    private static String bytesToHex(byte[] hash) {
        final var hexString = new StringBuilder(2 * hash.length);
        for (var b : hash) {
            var hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public enum Algorithm {

        SHA_256("SHA-256"),
        SHA3_256("SHA3-256");

        private final String asString;

        Algorithm(String asString) {
            this.asString = asString;
        }
    }
}

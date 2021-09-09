package com.dnb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;

import static java.util.stream.Collectors.*;

public class Base64EncoderAndDecoder {

    private static final Logger LOGGER = LogManager.getLogger(Base64EncoderAndDecoder.class);
    private static final String KEY_BASE_64 = "V2Vsa29tMTIz";

    public static void main(String[] args) {
        var base64EncoderAndDecoder = new Base64EncoderAndDecoder();
        base64EncoderAndDecoder.base64DecodeAndEncodeReturnDecoded(KEY_BASE_64);
        }

     String base64DecodeAndEncodeReturnDecoded(String title, String key) {
        LOGGER.info(title);
        String decoded = base64DecodeAndEncodeReturnDecoded(key);
        LOGGER.info("\n");
        return decoded;
    }

    String base64DecodeAndEncodeReturnDecoded(String keyBase64) {
        LOGGER.info(() -> String.format("Original encoded: %s%n%n", keyBase64));
        String decodedKey = new String(Base64.getDecoder().decode(keyBase64));
        LOGGER.info(() -> String.format("Base 64 decoded string: %s%n%n ", decodedKey));
        String encodedKey = Base64.getEncoder().encodeToString(decodedKey.getBytes());
        LOGGER.info(() -> String.format("Base 64 encoded: %s%n%n", encodedKey));
        return decodedKey;
    }

    static boolean containsCharsInOrder(final String containingString, String stringToCheck) {
        final var formattedContainingString = containingString.chars()
                .mapToObj(i -> String.valueOf((char) i))
                .filter(stringToCheck::contains)
                .collect(joining());
        LOGGER.info("Formatted containing string: {}", formattedContainingString);
        return formattedContainingString.contains(stringToCheck);
    }

    String base64Encode(String stringToEncode) {
        return Base64.getEncoder().encodeToString(stringToEncode.getBytes());
    }

    String base64Decode(String stringToDecode) {
        return new String(Base64.getDecoder().decode(stringToDecode));
    }
}

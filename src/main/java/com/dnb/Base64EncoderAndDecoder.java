package com.dnb;

import java.util.Base64;

import static java.lang.System.*;

public class Base64EncoderAndDecoder {

    private static final String KEY_BASE_64 = "V2Vsa29tMTIz";

    public static void main(String[] args) {
        var base64EncoderAndDecoder = new Base64EncoderAndDecoder();
        base64EncoderAndDecoder.base64DecodeAndEncode(KEY_BASE_64);
        }

     void base64DecodeAndEncode(String title, String key) {
        out.println(title);
        base64DecodeAndEncode(key);
        out.println();
    }

    void base64DecodeAndEncode(String keyBase64) {
        out.printf("Original encoded: %s%n%n", keyBase64);
        String decodedKey = new String(Base64.getDecoder().decode(keyBase64));
        out.printf("Base 64 decoded string: %s%n%n ", decodedKey);
        String encodedKey = Base64.getEncoder().encodeToString(decodedKey.getBytes());
        out.printf("Base 64 encoded: %s%n%n", encodedKey);
    }

}

package com.dnb;

public class StringManipulation {

    int startIndex(String string, String subString) {
        return string.indexOf(subString);
    }

    int endIndex(String string, char character) {
        return string.lastIndexOf(character);
    }

}

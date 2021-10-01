package com.dnb;

public enum EnumWithConstants {

    GET_ACCOUNT("camt.003.001.07"),
    RETURN_ACCOUNT("camt.004.001.08"),
    GET_VALUE_OF_MINIMUM_RESERVE(Constants.CAMT_998_001_03),
    INSERT_VALUE_OF_MINIMUM_RESERVE(Constants.CAMT_998_001_03),
    RETURN_VALUE_OF_MINIMUM_RESERVE(Constants.CAMT_998_001_03);

    private final String identifier;

    EnumWithConstants(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    private static class Constants {
        private static final String CAMT_998_001_03 = "camt.998.001.03";
    }
}

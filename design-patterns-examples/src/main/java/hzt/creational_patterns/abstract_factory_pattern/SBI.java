package hzt.creational_patterns.abstract_factory_pattern;

class SBI implements Bank {

    private final String bankName;

    public SBI() {
        bankName = "SBI BANK";
    }

    public String getBankName() {
        return bankName;
    }
}

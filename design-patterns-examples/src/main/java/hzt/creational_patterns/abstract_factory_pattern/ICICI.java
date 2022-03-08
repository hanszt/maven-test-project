package hzt.creational_patterns.abstract_factory_pattern;

class ICICI implements Bank {

    private final String bankName;

    ICICI() {
        bankName = "ICICI BANK";
    }

    public String getBankName() {
        return bankName;
    }
}

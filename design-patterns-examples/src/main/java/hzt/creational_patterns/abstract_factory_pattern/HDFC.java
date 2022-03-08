package hzt.creational_patterns.abstract_factory_pattern;

class HDFC implements Bank{

    private final String bankName;

    public HDFC(){
        bankName ="HDFC BANK";
    }

    public String getBankName() {
        return bankName;
    }
}

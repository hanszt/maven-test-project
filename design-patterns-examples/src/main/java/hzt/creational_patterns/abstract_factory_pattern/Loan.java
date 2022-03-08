package hzt.creational_patterns.abstract_factory_pattern;

abstract class Loan {
    public static final int MONTHS_IN_YEAR = 12;
    public static final int RATE_FACTOR = 1200;
    protected double rate;

    abstract void getInterestRate(double rate);

    public void calculateLoanPayment(double loanAmount, int years) {
        int months = years * MONTHS_IN_YEAR;
        rate = rate / RATE_FACTOR;
        double emi = ((rate * Math.pow((1 + rate), months)) / ((Math.pow((1 + rate), months)) - 1)) * loanAmount;

        System.out.println("your monthly EMI is " + emi + " for the amount" + loanAmount + " you have borrowed");
    }
}

package hzt.serialization;

import java.time.LocalDate;
import java.util.Objects;

// q39 test 5
// does not implement Serializable
class Bond {

    String ticker;
    double coupon;
    LocalDate maturity;

    public Bond(String ticker, double coupon, LocalDate maturity) {
        this.ticker = ticker;
        this.coupon = coupon;
        this.maturity = maturity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bond bond = (Bond) o;
        return Double.compare(bond.coupon, coupon) == 0 &&
                Objects.equals(ticker, bond.ticker) &&
                Objects.equals(maturity, bond.maturity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticker, coupon, maturity);
    }

    @Override
    public String toString() {
        return "Bond{" +
                "ticker='" + ticker + '\'' +
                ", coupon=" + coupon +
                ", maturity=" + maturity +
                '}';
    }
}

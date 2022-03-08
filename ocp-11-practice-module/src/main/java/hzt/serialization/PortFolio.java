package hzt.serialization;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
// q39 test 5
//If for any reason, you want to serialize Portfolio objects without making Bond class Serializable,
// you can customize the serialization of a Portfolio class by implementing
// readObject and writeObject methods as shown below:
public class PortFolio implements Serializable {

    private static final long serialVersionUID = 4;

    private final String accountName;
    private transient Bond[] bonds; // must be transient because Bond class does not implement Serializable

    public PortFolio(String accountName, Bond[] bonds) {
        this.accountName = accountName;
        this.bonds = bonds;
    }

    private void writeObject(ObjectOutputStream os) throws IOException {
        os.defaultWriteObject();
        os.writeInt(bonds.length);
        //write the state of bond objects
        for (Bond bond : bonds) {
            os.writeObject(bond.ticker);
            os.writeDouble(bond.coupon);
            os.writeObject(bond.maturity);
        }
    }

    private void readObject(ObjectInputStream os) throws IOException, ClassNotFoundException {
        os.defaultReadObject();
        bonds = new Bond[os.readInt()];
        //read the state of bond objects.
        for (int i = 0; i < bonds.length; i++) {
            String ticker = (String) os.readObject();
            double coupon = os.readDouble();
            LocalDate localDate = (LocalDate) os.readObject();
            bonds[i] = new Bond(ticker, coupon, localDate);
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public Bond[] getBonds() {
        return bonds;
    }
}

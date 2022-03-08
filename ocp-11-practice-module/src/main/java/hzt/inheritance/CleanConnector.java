package hzt.inheritance;

import java.io.IOException;
import java.sql.SQLException;

public class CleanConnector extends PortConnector {

    // Source: Enthuware Java11OCP Test 1 Q 37
    public CleanConnector(int port) throws IOException, SQLException {
        super(port);
        if (port > 300) {
            throw new SQLException();
        }
    }

    @Override
    public void connect(String number) {
        System.out.println("Connected to: " + number);
    }

    /*
    As PortConnector has only one constructor, there is only one way to instantiate it.
     Now, to instantiate any subclass of PortConnector, the subclass's constructor should call super(int).
     But that throws IOException.
     And so this exception (or its super class) must be defined in the 'throws' clause of subclass's constructor.
     Note that you cannot do something like:

     public CleanConnector(){ try{ super(); }catch(Exception e){} //WRONG :

     call to super must be first statement in constructor }

     Remember: Constructor must declare all the checked exceptions declared in the base constructor
     (or the super classes of the checked exceptions).
     They may add other exceptions as well. This behavior is exactly opposite to that of methods.
     The overriding method cannot throw any checked exception other than what the overridden method throws.
     It may throw subclasses of those exceptions as well.
    */
}

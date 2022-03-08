import hzt.service_provider_modules.Adder;
import hzt.interfaces.FunctionalInterfaceContainingMoreThanOneAbstractMethod;
import hzt.interfaces.MyFilter;
import hzt.service_provider_modules.RandomFilterProvider;

open module EnthuwareOCPStudyGroup {

    requires java.sql;

    exports hzt.interfaces;
    exports hzt.service_provider_modules;

    // These 'uses' clauses are necessary for the serviceloader tests
    // Source: Enthuware Java11OCP Test 1 Q 13, Test 1 Q 30
    uses String;
    //Service provider declaration
    // Source: Enthuware Java11OCP Test 1 Q 21
    provides FunctionalInterfaceContainingMoreThanOneAbstractMethod with Adder;
    provides MyFilter with RandomFilterProvider;
}

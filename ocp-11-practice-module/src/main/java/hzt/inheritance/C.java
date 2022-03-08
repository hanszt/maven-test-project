package hzt.inheritance;

public class C extends B {

    @Override
    public String classNameAsString() {
        return C.class.getSimpleName();
    }

    public String classNameViaSuperCall() {
        return super.classNameAsString();
    }
}

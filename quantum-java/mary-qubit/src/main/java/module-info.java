module mary.qubit {

    requires javafx.controls;
    requires org.slf4j;
    requires strange;
    requires strangefx;

    exports org.hzt to javafx.graphics;

    opens org.hzt;
}

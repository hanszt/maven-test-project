module kotlin.samples {
    requires kotlin.stdlib;
    requires kotlinx.coroutines.core.jvm;
    requires hzt.utils.core;

    // required for junit 5 to work when methods package private in java via maven surefire plugin
    opens org.hzt.coroutines.sequences;
}

package hzt.creational_patterns.singleton_pattern;

import java.io.Serializable;

import static hzt.creational_patterns.singleton_pattern.LazyInstantiation.getSingleton;

public class SerializationInSingletonPattern implements Serializable {

    //your code of singleton
    protected Object readResolve() {
        return getSingleton();
    }

}

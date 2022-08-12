package hzt.creational_patterns.singleton_pattern;

import java.io.Serial;
import java.io.Serializable;

import static hzt.creational_patterns.singleton_pattern.LazyInstantiation.getUnsafeLazyInstance;

public class SerializationInSingletonPattern implements Serializable {

    @Serial
    private static final long serialVersionUID = 4L;

    /**
     * readResolve is used for replacing the object read from the stream.
     * The only use I've ever seen for this is enforcing singletons; when an object is read, replace it with the singleton instance.
     * This ensures that nobody can create another instance by serializing and deserializing the singleton.
     *
     * @see <a href="https://stackoverflow.com/questions/1168348/java-serialization-readobject-vs-readresolve">
     *     Java serialization: readObject() vs. readResolve()</a>
     *
     * @return a serialized version of the singleton
     */
    //your code of singleton
    @Serial
    protected Object readResolve() {
        return getUnsafeLazyInstance();
    }

}

package hzt.annotations;

//The order of values for the elements is not important.
@MyArtifact(name = "Test", id = 3)
public class AnnotationsTarget {

    @MyArtifact(id = 4, name = "TestMethod")
    void testMethod() {

    }
}

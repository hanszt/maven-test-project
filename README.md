# Maven test project

---

This is a project to test and explore Java conventions and concepts

Author: Hans Zuidervaart

---

## Modules

This project consists of multiple modules:

1. [Design pattern examples](design-patterns-examples/README.md)
2. [Selenium samples](selenium-samples/README.md)
3. [Java 17 practice module](java-17-practice-module/README.md)
4. [Ocp 11 practice module](ocp-11-practice-module/README.md)
5. [Other tests](other-tests/README.md)
6. [Reactive programming](reactive-streams/README.md)
7. [Scheduling samples](scheduling-samples/README.md)
8. [Tomcat servlet filter](java-web/README.md)
9. [Apache Kafka Samples](kafka-samples/README.md)
10. [Transaction-samples](transaction-samples/README.md)
11. [Pdf exporters](pdf-exporters/README.md)
12. [Programming puzzles][file:programming-puzzles-readme]
13. [Latest jdk practice module](latest-jdk-practice-module/README.md)
14. [quantum-java](quantum-java/README.md)
15. [math-utils-samples](math-utils-samples/README.md)
16. [benchmark](benchmark/README.md)

The module [Programming puzzles][file:programming-puzzles-readme] contains also separate modules with different types of puzzels.

[file:programming-puzzles-readme]:programming-puzzles/README.md

---

## Requirements

Some modules require the following custom dependencies:

```xml

<dependencies>
    <dependency>
        <groupId>org.hzt.utils</groupId>
        <artifactId>test-data-generator</artifactId>
        <version>${test-data-generator.version}</version>
    </dependency>
    <dependency>
        <groupId>org.hzt.utils</groupId>
        <artifactId>core</artifactId>
        <version>${org.hzt-utils.version}</version>
    </dependency>
</dependencies>
```

See the [pom](pom.xml)

These dependencies are not yet available at maven central. 

These dependencies are available in the [local maven repo](_local-mvn-repo) that is present in the project

You can also use these dependencies by locally installing the
[hzt.utils](https://github.com/hanszt/hzt-utils) project modules using `mvm clean install`

---

### Running tests

Run `mvn clean test` to run all tests using mvn

If you get a module not found error while trying to run the tests via intellij,
then first do a `mvn clean install` from here. After that,
check that your intellij Maven-home-path is set to your local maven installation.

---

## Cleaning all modules

There is a [clean all](clean-all.cmd) file which can be used to clean all existing modules in this project.

If it fails, first run `mvn clean install` from this root

---

## Running individual modules

Use the [Maven advanced reactor options](https://blog.sonatype.com/2009/10/maven-tips-and-tricks-advanced-reactor-options/), more specifically:

````
-pl, --projects
Build specified reactor projects instead of all projects
-am, --also-make
If project list is specified, also build projects required by the list
````

So just cd into the parent P directory and run:

````
mvn install -pl B -am
````

And this will build B and the modules required by B.

Note that you need to use a colon if you are referencing an artifactId which differs from the directory name:

````
mvn install -pl :B -am
````

As described here:

- [Define modules list which shall be build in Maven multiproject build](https://stackoverflow.com/questions/26429476/define-modules-list-which-shall-be-build-in-maven-multiproject-build/26439938#26439938)

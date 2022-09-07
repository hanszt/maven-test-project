# Maven test project

---

This is a project to test and explore Java conventions and concepts

Author: Hans Zuidervaart

---

## Modules

This project consists of multiple modules:

- [Design pattern examples](design-patterns-examples/README.md)
- [Selenium samples](selenium-samples/README.md)
- [Java 17 practice module](java-17-practice-module/README.md)
- [Ocp 11 practice module](ocp-11-practice-module/README.md)
- [Other tests](other-tests/README.md)
- [Reactive programming](reactive-streams/README.md)
- [Scheduling samples](scheduling-samples/README.md)
- [Tomcat servlet filter](java-web/README.md)
- [Transaction-samples](transaction-samples/README.md)
- [Jasper Reports](jasper-reports/README.md)

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

These dependencies are not yet available at maven central. You can use these dependencies by locally installing the
[hzt.utils](https://github.com/hanszt/hzt-utils) project modules using `mvm clean install`

---

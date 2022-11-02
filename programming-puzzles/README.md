## Module to participate in programming puzzles

---

Author: Hans Zuidervaart

---

## Modules

This project contains the following modules:

- [puzzle sites](puzzle-sites/README.md)
- [other puzzles](other-puzzles/README.md)
- [assessment navara](assessment-navara/README.md)
- [benchmarks](puzzle-benchmark/README.md)

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

package hzt.only_jdk.records;

import java.util.Collections;
import java.util.List;

record ImmutableRecord(String name, List<String> roles) {

    ImmutableRecord {
        roles = Collections.unmodifiableList(roles);
    }
}

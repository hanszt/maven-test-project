package hzt.only_jdk.records;

import java.util.List;

record ImmutableRecord(String name, List<String> roles) {

    ImmutableRecord {
        roles = List.copyOf(roles);
    }
}

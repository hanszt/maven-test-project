package hzt.behavioural_patterns.iterator_pattern.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profile {

    private final String name;
    private final String email;
    private final Map<String, List<String>> contacts = new HashMap<>();

    public Profile(String email, String name, String... contacts) {
        this.email = email;
        this.name = name;

        parseContactList(contacts);
    }

    private void parseContactList(String[] contacts) {
        for (String contact : contacts) {
            String[] parts = contact.split(":");
            String contactType = "friend";
            String contactEmail;
            if (parts.length > 1) {
                contactType = parts[0];
                contactEmail = parts[1];
            } else contactEmail = parts[0];
            this.contacts.computeIfAbsent(contactType, k -> new ArrayList<>()).add(contactEmail);
        }
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public List<String> getContacts(String contactType) {
        return contacts.computeIfAbsent(contactType, k -> new ArrayList<>());
    }
}

package hzt.behavioural_patterns.iterator_pattern.social_networks;

import hzt.behavioural_patterns.iterator_pattern.iterators.LinkedInIterator;
import hzt.behavioural_patterns.iterator_pattern.iterators.ProfileIterator;
import hzt.behavioural_patterns.iterator_pattern.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@SuppressWarnings(LinkedIn.REPLACE_SYSTEM_OUT_BY_LOGGER)
public class LinkedIn implements SocialNetwork {

    static final String REPLACE_SYSTEM_OUT_BY_LOGGER = "squid:S106";
    private final List<Profile> contacts;

    public LinkedIn(List<Profile> cache) {
        this.contacts = Objects.requireNonNullElseGet(cache, ArrayList::new);
    }

    public Profile requestContactInfoFromLinkedInAPI(String profileEmail) {
        // Here would be a POST request to one of the LinkedIn API endpoints.
        // Instead, we emulates long network connection, which you would expect
        // in the real life...
        simulateNetworkLatency();
        System.out.println("LinkedIn: Loading profile '" + profileEmail + "' over the network...");

        // ...and return test data.
        return findContact(profileEmail);
    }

    public List<String> requestRelatedContactsFromLinkedInAPI(String profileEmail, String contactType) {
        // Here would be a POST request to one of the LinkedIn API endpoints.
        // Instead, we emulates long network connection, which you would expect
        // in the real life.
        simulateNetworkLatency();
        System.out.println("LinkedIn: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");

        // ...and return test data.
        Profile profile = findContact(profileEmail);
        if (profile != null) {
            return profile.getContacts(contactType);
        }
        return Collections.emptyList();
    }

    private Profile findContact(String profileEmail) {
        for (Profile profile : contacts) {
            if (profile.getEmail().equals(profileEmail)) {
                return profile;
            }
        }
        return null;
    }

    private static void simulateNetworkLatency() {
        try {
            TimeUnit.NANOSECONDS.sleep(2500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public ProfileIterator createContactsIterator(String profileEmail) {
        return new LinkedInIterator(this, profileEmail);
    }
}

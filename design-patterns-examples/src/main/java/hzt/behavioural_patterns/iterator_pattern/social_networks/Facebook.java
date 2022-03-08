package hzt.behavioural_patterns.iterator_pattern.social_networks;

import hzt.behavioural_patterns.iterator_pattern.iterators.FacebookIterator;
import hzt.behavioural_patterns.iterator_pattern.iterators.ProfileIterator;
import hzt.behavioural_patterns.iterator_pattern.profile.Profile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Facebook implements SocialNetwork {

    private final List<Profile> profiles;

    public Facebook(List<Profile> cache) {
        this.profiles = Objects.requireNonNullElseGet(cache, ArrayList::new);
    }

    public Profile requestProfileFromFacebook(String profileEmail) {
        // Here would be a POST request to one of the Facebook API endpoints.
        // Instead, we emulates long network connection, which you would expect
        // in the real life...
        simulateNetworkLatency();
        System.out.println("Facebook: Loading profile '" + profileEmail + "' over the network...");

        // ...and return test data.
        return findProfile(profileEmail);
    }

    public List<String> requestProfileFriendsFromFacebook(String profileEmail, String contactType) {
        // Here would be a POST request to one of the Facebook API endpoints.
        // Instead, we emulates long network connection, which you would expect
        // in the real life...
        simulateNetworkLatency();
        System.out.println("Facebook: Loading '" + contactType + "' list of '" + profileEmail + "' over the network...");

        // ...and return test data.
        Profile profile = findProfile(profileEmail);
        if (profile != null) {
            return profile.getContacts(contactType);
        }
        return Collections.emptyList();
    }

    private Profile findProfile(String profileEmail) {
        for (Profile profile : profiles) {
            if (profile.getEmail().equals(profileEmail)) {
                return profile;
            }
        }
        return null;
    }

    private void simulateNetworkLatency() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public ProfileIterator createContactsIterator(String profileEmail) {
        return new FacebookIterator(this, profileEmail);
    }

}

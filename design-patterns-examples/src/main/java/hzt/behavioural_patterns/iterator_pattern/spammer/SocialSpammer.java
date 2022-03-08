package hzt.behavioural_patterns.iterator_pattern.spammer;

import hzt.behavioural_patterns.iterator_pattern.iterators.FacebookIterator;
import hzt.behavioural_patterns.iterator_pattern.iterators.LinkedInIterator;
import hzt.behavioural_patterns.iterator_pattern.iterators.ProfileIterator;
import hzt.behavioural_patterns.iterator_pattern.profile.Profile;
import hzt.behavioural_patterns.iterator_pattern.social_networks.SocialNetwork;

public class SocialSpammer {

    private final SocialNetwork network;

    public SocialSpammer(SocialNetwork network) {
        this.network = network;
    }

    public void sendSpamToContacts(String profileEmail, String message) {
        ProfileIterator iterator = network.createContactsIterator(profileEmail);
        String type = "profiles";
        if (iterator instanceof FacebookIterator) type = "friends";
        else if (iterator instanceof LinkedInIterator) type = "coworkers";
        System.out.println("\nIterating over " + type + "...\n");
        while (iterator.hasNext()) {
            Profile profile = iterator.getNext();
            sendMessage(profile.getEmail(), message);
        }
    }

    public void sendMessage(String email, String message) {
        System.out.println("Sent message to: '" + email + "'. Message body: '" + message + "'");
    }
}

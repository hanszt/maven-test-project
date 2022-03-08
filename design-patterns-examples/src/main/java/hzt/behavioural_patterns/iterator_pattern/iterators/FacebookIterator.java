package hzt.behavioural_patterns.iterator_pattern.iterators;

import hzt.behavioural_patterns.iterator_pattern.profile.Profile;
import hzt.behavioural_patterns.iterator_pattern.social_networks.Facebook;

import java.util.ArrayList;
import java.util.List;

public class FacebookIterator implements ProfileIterator {

    private final Facebook facebook;
    private final String email;
    private int currentPosition = 0;
    private final List<String> emails = new ArrayList<>();
    private final List<Profile> contacts = new ArrayList<>();

    public FacebookIterator(Facebook facebook, String email) {
        this.facebook = facebook;
        this.email = email;
    }

    private void lazyLoad() {
        if (emails.isEmpty()) {
            List<String> profiles = facebook.requestProfileFriendsFromFacebook(this.email, "friends");
            for (String profile : profiles) {
                this.emails.add(profile);
                this.contacts.add(null);
            }
        }
    }

    @Override
    public boolean hasNext() {
        lazyLoad();
        return currentPosition < emails.size();
    }

    @Override
    public Profile getNext() {
        if (!hasNext()) {
            return null;
        }

        String friendEmail = emails.get(currentPosition);
        Profile friendProfile = contacts.get(currentPosition);
        if (friendProfile == null) {
            friendProfile = facebook.requestProfileFromFacebook(friendEmail);
            contacts.set(currentPosition, friendProfile);
        }
        currentPosition++;
        return friendProfile;
    }

    @Override
    public void reset() {
        currentPosition = 0;
    }

    @Override
    public String toString() {
        return "FacebookIterator{}";
    }
}

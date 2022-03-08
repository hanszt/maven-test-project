package hzt.behavioural_patterns.iterator_pattern.social_networks;

import hzt.behavioural_patterns.iterator_pattern.iterators.ProfileIterator;

public interface SocialNetwork {

    ProfileIterator createContactsIterator(String profileEmail);
}

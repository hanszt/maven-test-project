package hzt.behavioural_patterns.iterator_pattern.iterators;

import hzt.behavioural_patterns.iterator_pattern.profile.Profile;

public interface ProfileIterator {
    boolean hasNext();

    Profile getNext();

    void reset();
}

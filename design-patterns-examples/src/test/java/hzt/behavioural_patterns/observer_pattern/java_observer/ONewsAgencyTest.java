package hzt.behavioural_patterns.observer_pattern.java_observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ONewsAgencyTest {

    @Test
    void testObservablePattern() {

        ONewsAgency publisher = new ONewsAgency();
        ONewsChannel subscriber = new ONewsChannel();

        publisher.addObserver(subscriber);
        publisher.setNews("news");
        assertEquals("news", subscriber.getNews());
    }

}

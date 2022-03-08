package hzt.behavioural_patterns.observer_pattern.java_pcl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PCLNewsChannelTest {

    @Test
    void testObserverPatternUptoDateApi() {
        PCLNewsAgency observable = new PCLNewsAgency();
        PCLNewsChannel observer = new PCLNewsChannel();

        observable.addPropertyChangeListener(observer);
        observable.setNews("news");

        assertEquals("news", observer.getNews());
    }
}

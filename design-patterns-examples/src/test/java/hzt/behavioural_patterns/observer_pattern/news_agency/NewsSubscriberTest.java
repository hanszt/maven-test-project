package hzt.behavioural_patterns.observer_pattern.news_agency;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NewsSubscriberTest {

    @Test
    void testObserverPatternCustomImplementation() {
        NewsAgency observable = new NewsAgency();
        NewsSubscriber observer = new NewsSubscriber();

        observable.addObserver(observer);
        observable.setNews("news");
        assertEquals("news", observer.getNews());
    }

}

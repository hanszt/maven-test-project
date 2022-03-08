package hzt.behavioural_patterns.observer_pattern.java_observer;

import java.util.Observable;
import java.util.Observer;

public class ONewsChannel implements Observer {

    private String news;

    @Override
    public void update(Observable o, Object news) {
        this.news = (String) news;
    }

    public String getNews() {
        return news;
    }
}

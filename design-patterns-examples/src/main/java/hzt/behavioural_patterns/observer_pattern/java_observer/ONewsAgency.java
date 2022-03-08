package hzt.behavioural_patterns.observer_pattern.java_observer;

import java.util.Observable;

public class ONewsAgency extends Observable {

    public void setNews(String news) {
        setChanged();
        notifyObservers(news);
    }


}

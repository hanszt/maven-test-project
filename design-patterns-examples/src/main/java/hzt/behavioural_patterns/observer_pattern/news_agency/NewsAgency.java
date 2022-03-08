package hzt.behavioural_patterns.observer_pattern.news_agency;

import java.util.ArrayList;
import java.util.List;

public class NewsAgency {

    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    public void setNews(String news) {
        for (Observer observer : this.observers) {
            observer.update(news);
        }
    }
}

package hzt.behavioural_patterns.observer_pattern.news_agency;

public class NewsSubscriber implements Observer {

    private String news;

    @Override
    public void update(Object news) {
        this.news = ((String) news);
    }

    public String getNews() {
        return news;
    }

}

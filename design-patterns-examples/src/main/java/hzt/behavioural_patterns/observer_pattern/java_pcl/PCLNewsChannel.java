package hzt.behavioural_patterns.observer_pattern.java_pcl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PCLNewsChannel implements PropertyChangeListener {

    private String news;

    public void propertyChange(PropertyChangeEvent evt) {
        this.news = ((String) evt.getNewValue());
    }

    public String getNews() {
        return news;
    }
}

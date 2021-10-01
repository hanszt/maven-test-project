package hzt.stream.model;

import java.io.Serializable;
import java.util.Objects;

public class Book implements Serializable {

    private static final long serialVersionUID = 1;

    private String title;
    private transient String description;
    private transient int copies;
    private final transient String category;
    private transient String language = "Java";

    public Book(String category) {
        this("", category);
    }

    public Book(String title, String category) {
        this.title = title;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public String getCategory() {
        return category;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean hasCopies() {
        return copies > 0;
    }

    public boolean isAboutProgramming() {
        return "Programming".equals(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Book book = (Book) o;
        return Objects.equals(title, book.title) && Objects.equals(category, book.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, category);
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookName='" + title + '\'' +
                ", description='" + description + '\'' +
                ", copies=" + copies +
                ", bookCategory='" + category + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}

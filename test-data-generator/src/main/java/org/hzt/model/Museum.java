package org.hzt.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Museum {

    private final String name;
    private final LocalDate dateOfOpening;
    private final List<Painting> paintingList = new ArrayList<>();

    private Painting mostPopularPainting;

    public Museum(String name, LocalDate dateOfOpening, List<Painting> paintingList) {
        this.name = name;
        this.dateOfOpening = dateOfOpening;
        this.paintingList.addAll(paintingList);
        this.mostPopularPainting = paintingList.stream()
                .findAny()
                .orElseThrow();
    }

    public String getName() {
        return name;
    }

    public LocalDate getDateOfOpening() {
        return dateOfOpening;
    }

    public Painting getOldestPainting() {
        return paintingList.stream()
                .min(Comparator.comparing(Painting::getYearOfCreation))
                .orElseThrow();
    }

    public Painting getMostPopularPainting() {
        return mostPopularPainting;
    }

    public void setMostPopularPainting(Painting mostPopularPainting) {
        this.mostPopularPainting = mostPopularPainting;
    }

    public List<Painting> getPaintingList() {
        return Collections.unmodifiableList(paintingList);
    }

    public void toDatesOfBirthPainters(Consumer<LocalDate> dateOfBirthConsumer) {
        paintingList.stream()
                .map(Painting::painter)
                .map(Painter::getDateOfBirth)
                .filter(Objects::nonNull)
                .forEach(dateOfBirthConsumer);
    }

    @Override
    public String toString() {
        return "Museum{" +
                "name='" + name + '\'' +
                ", dateOfOpening=" + dateOfOpening +
                ", mostPopularPainting=" + mostPopularPainting +
                '}';
    }
}

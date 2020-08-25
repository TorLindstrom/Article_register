package org.example;

import java.time.LocalDateTime;

import static org.example.Category.*;

public class Entry {
    private String name;
    private Category category;
    private int price;
    private LocalDateTime timestamp;


    public Entry(String name, Category category, int price, LocalDateTime timestamp) {
        this(name, category, price, false);
        this.timestamp = timestamp;
    }

    public Entry(String name, Category category, int price, boolean setTime) {
        this.name = name;
        this.category = category;
        this.price = price;
        if (setTime) {
            this.timestamp = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        //TODO: set so that every entry get to same length when toStringed()
        return name + "\t" + category + "\t" + price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public int getInvertedPrice() {
        return -price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}


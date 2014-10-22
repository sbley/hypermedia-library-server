package de.saxsys.campus.domain;

public class Book {

    private int id;
    private String title;
    private String author;
    private String description;

    public Book(int id, String title, String author, String description) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public boolean matchesTitle(String query) {
        return title.toLowerCase().contains(query.toLowerCase());
    }

    public boolean matchesAuthor(String query) {
        return author.toLowerCase().contains(query.toLowerCase());
    }
}
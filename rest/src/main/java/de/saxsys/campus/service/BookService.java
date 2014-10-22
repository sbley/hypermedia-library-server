package de.saxsys.campus.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import de.saxsys.campus.domain.Book;

@ApplicationScoped
public class BookService {

    private Map<Integer, Book> books;

    @PostConstruct
    public void init() {
        books = new HashMap<>();
        books.put(
                1,
                new Book(
                        1,
                        "Pretty Honest",
                        "Sally Hughes",
                        "A witty, wise and truthful beauty handbook for real women on what works in real life from Sali Hughes, beloved journalist and broadcaster."));
        books.put(
                2,
                new Book(
                        2,
                        "Guiness World Records 2015",
                        "Guiness World Records ",
                        "The world's best-selling annual is back and crammed with thousands of amazing new records, cool facts and awesome pictures!"));
        books.put(
                3,
                new Book(
                        3,
                        "The Second Half",
                        "Roy Keane",
                        "In an eighteen-year playing career for Cobh Ramblers, Nottingham Forest (under Brian Clough), Manchester United (under Sir Alex Ferguson) and Celtic, Roy Keane dominated every midfield he led to glory."));
        books.put(
                4,
                new Book(
                        4,
                        "RESTful Web APIs",
                        "Leonard Richardson",
                        "The popularity of REST in recent years has led to tremendous growth in almost-RESTful APIs that don’t include many of the architecture’s benefits."));
    }

    public List<Book> find(String query) {
        List<Book> result = new ArrayList<>();
        for (Book b : books.values()) {
            if (b.matchesTitle(query) || b.matchesAuthor(query)) {
                result.add(b);
            }
        }
        return result;
    }

}

package de.saxsys.hypermedia.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import de.saxsys.hypermedia.domain.Book;
import de.saxsys.hypermedia.domain.Member;

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
        books.put(
                5,
                new Book(
                        5,
                        "Lonely Planet Japan",
                        "Lonely Planet",
                        "Lonely Planet Japan is your passport to all the most relevant and up-to-date advice on what to see, what to skip, and what hidden discoveries await you."));
        books.put(
                6,
                new Book(
                        6,
                        "Mushrooms",
                        "Roger Phillips",
                        "The culmination of over thirty years' work, this authoritative and superbly illustrated reference work is packed with the most up-to-date information and original photographs. Set to become the essential illustrated mycological encyclopedia for the next 25 years, this book is also clear, user friendly and will appeal to a wide range of readers."));
        books.put(
                7,
                new Book(
                        7,
                        "Just Bento Cookbook",
                        "Makiko Itoh",
                        "Bento fever is sweeping the world fuelled by its promise of superb food that is economical and healthy in these tough economic times. The Just Bento Cookbook contains 25 attractive bento menus and more than 150 recipes, all of which have been specially created for this book."));
        books.put(
                8,
                new Book(
                        8,
                        "The Woman Who Stole My Life",
                        "Marian Keyes",
                        "One day, sitting in traffic, married Dublin mum Stella Sweeney attempts a good deed. The resulting car crash changes her life."));
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

    public Book get(int id) {
        return books.get(id);
    }

    public Book lend(int bookId, Member member) throws AlreadyLentException {
        Book book = books.get(bookId);
        if (null != book) {
            if (book.isLent() && book.getBorrower() != member)
                throw new AlreadyLentException(book.getBorrower().getId());
            else
                book.lendTo(member);
        }
        return book;
    }

    public Book takeBack(int bookId) throws NotLentException {
        Book book = books.get(bookId);
        if (null != book) {
            if (!book.isLent()) {
                throw new NotLentException();
            } else
                book.takeBack();
        }
        return book;
    }

}

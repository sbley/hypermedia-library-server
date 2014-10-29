package de.saxsys.hypermedia.service;

public class NotLentException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotLentException() {
        super("The book is not lent.");
    }
}

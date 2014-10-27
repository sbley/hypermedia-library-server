package de.saxsys.campus.service;

public class AlreadyLentException extends Exception {

    private static final long serialVersionUID = 1L;

    public AlreadyLentException(int borrowerId) {
        super("The book is already lent to member " + borrowerId);
    }
}

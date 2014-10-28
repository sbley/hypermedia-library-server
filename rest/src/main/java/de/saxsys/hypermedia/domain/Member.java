package de.saxsys.hypermedia.domain;

public class Member {

    int id;
    private String name;

    public Member(int id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

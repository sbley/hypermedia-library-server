package de.saxsys.hypermedia.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import de.saxsys.hypermedia.domain.Member;

@ApplicationScoped
public class MemberService {

    private Map<Integer, Member> members;

    @PostConstruct
    public void init() {
        members = new HashMap<>();
        members.put(1, new Member(1, "Jon Smith"));
        members.put(2, new Member(2, "Matt Hazelwood"));
        members.put(3, new Member(3, "Steve Baker"));
    }

    public Member get(int id) {
        return members.get(id);
    }
}

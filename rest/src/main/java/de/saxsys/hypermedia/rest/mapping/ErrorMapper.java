package de.saxsys.hypermedia.rest.mapping;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;

@Singleton
public class ErrorMapper {

    @Inject
    private RepresentationFactory rf;

    public Representation createRepresentation(String message, Throwable e) {
        return createRepresentation(message, e.getMessage());
    }

    public Representation createRepresentation(String message, String detail) {
        return rf.newRepresentation()
                .withProperty("status", "error")
                .withProperty("title", message)
                .withProperty("detail", detail);
    }
}

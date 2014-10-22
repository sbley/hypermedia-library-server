package de.saxsys.campus.rest.resource;

import static de.saxsys.campus.rest.LinkRelations.NAMESPACE;
import static de.saxsys.campus.rest.LinkRelations.NAMESPACE_HREF;
import static de.saxsys.campus.rest.LinkRelations.REL_BOOK;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import de.saxsys.campus.domain.Book;
import de.saxsys.campus.rest.hal.HalMediaTypes;
import de.saxsys.campus.service.BookService;

@RequestScoped
@Path("books")
public class BookResource {

    @Inject
    private RepresentationFactory rf;

    @Context
    private UriInfo uriInfo;

    @Inject
    private BookService bookService;

    @GET
    @Produces(HalMediaTypes.HAL_JSON)
    public Response getBooks(@QueryParam("q") String query) {
        Representation rep = rf.newRepresentation(uriInfo.getRequestUri());
        rep.withNamespace(NAMESPACE, NAMESPACE_HREF);
        List<Book> books = bookService.find(query);
        for (Book b : books) {
            rep.withRepresentation(REL_BOOK, rf.newRepresentation(createUri(b)).withBean(b));
        }
        return Response.ok(rep).build();
    }

    private URI createUri(Book book) {
        return UriBuilder.fromUri(uriInfo.getBaseUri()).path(BookResource.class).path("{id}").build(book.getId());
    }
}

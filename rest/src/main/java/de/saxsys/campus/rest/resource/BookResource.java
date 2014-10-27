package de.saxsys.campus.rest.resource;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import de.saxsys.campus.domain.Book;
import de.saxsys.campus.rest.LinkRelations;
import de.saxsys.campus.rest.hal.HalMediaTypes;
import de.saxsys.campus.rest.mapping.ErrorMapper;
import de.saxsys.campus.service.AlreadyLentException;
import de.saxsys.campus.service.BookService;
import de.saxsys.campus.service.NotLentException;

@RequestScoped
@Path("books")
public class BookResource {

    @Inject
    private RepresentationFactory rf;

    @Context
    private UriInfo uriInfo;

    @Inject
    private BookService bookService;

    @Inject
    private ErrorMapper errorMapper;

    @GET
    @Produces(HalMediaTypes.HAL_JSON)
    public Response getBooks(@QueryParam("q") String query) {
        Representation rep = rf.newRepresentation(uriInfo.getRequestUri());
        rep.withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF);
        List<Book> books = bookService.find(query);
        for (Book b : books) {
            rep.withRepresentation(LinkRelations.REL_BOOK, rf.newRepresentation(createUri(b)).withBean(b));
        }
        return Response.ok(rep).build();
    }

    @GET
    @Produces(HalMediaTypes.HAL_JSON)
    @Path("{id}")
    public Response getBook(@PathParam("id") int id) {
        Book book = bookService.get(id);
        if (null == book) {
            throw new WebApplicationException(404);
        }
        Representation rep =
                rf.newRepresentation(uriInfo.getRequestUri()).withBean(book)
                        .withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF)
                        .withLink(LinkRelations.REL_LEND, uriInfo.getRequestUri().toString() + "/borrower/{memberId}");
        return Response.ok(rep).build();
    }

    @PUT
    @Produces(HalMediaTypes.HAL_JSON)
    @Path("{bookId}/borrower/{memberId}")
    public Response lend(@PathParam("bookId") int bookId, @PathParam("memberId") int memberId) {
        Book book;
        try {
            book = bookService.lend(bookId, memberId);
            if (null == book) {
                throw new WebApplicationException(404);
            }
            Representation rep = rf.newRepresentation(uriInfo.getRequestUri()).withBean(book);
            return Response.ok(rep).build();
        } catch (AlreadyLentException e) {
            return Response.status(409).entity(errorMapper.createRepresentation("Unable to lend book", e)).build();
        }
    }

    @DELETE
    @Produces(HalMediaTypes.HAL_JSON)
    @Path("{bookId}/borrower/{memberId}")
    public Response takeBack(@PathParam("bookId") int bookId, @PathParam("memberId") int memberId) {
        Book book;
        try {
            book = bookService.takeBack(bookId, memberId);
            if (null == book) {
                throw new WebApplicationException(404);
            }
            Representation rep = rf.newRepresentation(uriInfo.getRequestUri()).withBean(book);
            return Response.ok(rep).build();
        } catch (NotLentException e) {
            return Response.status(404).entity(errorMapper.createRepresentation("Unable to return book", e)).build();
        }
    }

    private URI createUri(Book book) {
        return UriBuilder.fromUri(uriInfo.getBaseUri()).path(BookResource.class).path("{id}").build(book.getId());
    }
}

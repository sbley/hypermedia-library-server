package de.saxsys.hypermedia.rest.resource;

import static de.saxsys.hypermedia.rest.hal.HalMediaTypes.HAL_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.net.URI;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.Representation;
import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import de.saxsys.hypermedia.domain.Book;
import de.saxsys.hypermedia.domain.Member;
import de.saxsys.hypermedia.rest.LinkRelations;
import de.saxsys.hypermedia.rest.hal.HalUtil;
import de.saxsys.hypermedia.rest.mapping.ErrorMapper;
import de.saxsys.hypermedia.service.AlreadyLentException;
import de.saxsys.hypermedia.service.BookService;
import de.saxsys.hypermedia.service.MemberService;
import de.saxsys.hypermedia.service.NotLentException;

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
    private MemberService memberService;

    @Inject
    private ErrorMapper errorMapper;

    @GET
    @Produces(HAL_JSON)
    public Response getBooks(@QueryParam("q") String query, @Context Request request) {
        Representation rep = rf.newRepresentation(uriInfo.getRequestUri());
        rep.withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF);
        List<Book> books = bookService.find(query);
        for (Book b : books) {
            rep.withRepresentation(LinkRelations.REL_BOOK, createSimpleRep(b));
        }

        // create ETag
        EntityTag etag = new EntityTag(Integer.toString(rep.hashCode()));
        ResponseBuilder builder = request.evaluatePreconditions(etag);
        if (builder == null) {
            builder = Response.ok(rep).tag(etag);
        }

        return builder.build();
    }

    @GET
    @Produces(HAL_JSON)
    @Path("{id}")
    public Response getBook(@PathParam("id") int id) {
        Book book = bookService.get(id);
        if (null == book) {
            throw new WebApplicationException(404);
        }
        return Response.ok(createFullRep(book)).build();
    }

    @PUT
    @Produces(HAL_JSON)
    @Consumes({HAL_JSON, APPLICATION_JSON })
    @Path("{bookId}/borrower")
    public Response lend(@PathParam("bookId") int bookId, Representation repMember) {

        // check request data
        Integer memberId = HalUtil.toInt(repMember.getValue("memberId", null));
        if (null == memberId)
            return Response.status(400)
                    .entity(errorMapper.createRepresentation("Unable to lend book",
                            "Parameter memberId missing"))
                    .build();

        // check memberId
        Member member = memberService.get(memberId);
        if (null == member)
            return Response.status(400)
                    .entity(errorMapper.createRepresentation("Unable to lend book",
                            "Unknown memberId"))
                    .build();

        // check book
        Book book;
        try {
            book = bookService.lend(bookId, member);
            if (null == book)
                return Response.status(404)
                        .entity(errorMapper.createRepresentation("Unable to lend book",
                                "Unknown book"))
                        .build();

            return Response.ok(createFullRep(book)).build();
        } catch (AlreadyLentException e) {
            return Response.status(409)
                    .entity(errorMapper.createRepresentation("Unable to lend book", e))
                    .build();
        }
    }

    @DELETE
    @Produces(HAL_JSON)
    @Path("{bookId}/borrower")
    public Response takeBack(@PathParam("bookId") int bookId) {
        Book book;
        try {
            book = bookService.takeBack(bookId);
            if (null == book)
                return Response.status(404)
                        .entity(errorMapper.createRepresentation("Unable to return book",
                                "Unknown book"))
                        .build();

            return Response.ok(createFullRep(book)).build();
        } catch (NotLentException e) {
            return Response.status(404)
                    .entity(errorMapper.createRepresentation("Unable to return book", e))
                    .build();
        }
    }

    private URI createUri(Book book) {
        return UriBuilder.fromUri(uriInfo.getBaseUri())
                .path(BookResource.class)
                .path("{id}")
                .build(book.getId());
    }

    private Representation createLinkOnlyRep(Book book) {
        return rf.newRepresentation(createUri(book));
    }

    private Representation createSimpleRep(Book book) {
        return rf.newRepresentation(createUri(book))
                .withProperty("id", book.getId())
                .withProperty("title", book.getTitle())
                .withProperty("author", book.getAuthor());
    }

    private Representation createFullRep(Book book) {
        Representation rep =
                rf.newRepresentation(createUri(book))
                        .withProperty("id", book.getId())
                        .withProperty("title", book.getTitle())
                        .withProperty("author", book.getAuthor())
                        .withProperty("description", book.getDescription())
                        .withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF);
        if (book.isLent()) {
            rep.withLink(LinkRelations.REL_RETURN, createUri(book).toString() + "/borrower");
            rep.withRepresentation("borrower", createRep(book.getBorrower()));
        } else {
            rep.withLink(LinkRelations.REL_LEND, createUri(book).toString() + "/borrower");
        }
        return rep;
    }

    private Representation createRep(Member member) {
        return rf.newRepresentation().withBean(member);
    }
}

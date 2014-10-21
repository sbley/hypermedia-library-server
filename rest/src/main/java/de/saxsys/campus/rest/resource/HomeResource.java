package de.saxsys.campus.rest.resource;

import java.net.URI;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import de.saxsys.campus.rest.hal.HalMediaTypes;

@RequestScoped
@Path("/")
public class HomeResource {

    private static final String SLOTS = "c:slots";
    private static final String CURRENTUSER = "c:currentUser";

    /** home may be cached for five minutes */
    private static final int MAX_AGE_SECONDS = 300;

    @Inject
    private RepresentationFactory representationFactory;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(HalMediaTypes.HAL_JSON)
    public Response getHome() {
        URI baseUri = uriInfo.getBaseUri();
        return Response.ok(
                representationFactory
                        .newRepresentation(baseUri)
                        .withLink(SLOTS, UriBuilder.fromUri(baseUri).path(SlotResource.class).build())
                        .withNamespace("c", "http://localhost:8080/rest-docs/{rel}")
                        .withLink(CURRENTUSER,
                                UriBuilder.fromUri(baseUri).path(UserResource.class).path("current").build()))
        // .cacheControl(defaultCacheControl()) // TODO Caching
                .build();
    }

    private CacheControl defaultCacheControl() {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(MAX_AGE_SECONDS);
        return cc;
    }
}

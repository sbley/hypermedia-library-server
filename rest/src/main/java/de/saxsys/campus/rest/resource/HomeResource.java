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

import de.saxsys.campus.rest.LinkRelations;
import de.saxsys.campus.rest.hal.HalMediaTypes;

@RequestScoped
@Path("/")
public class HomeResource {

    @Inject
    private RepresentationFactory rf;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(HalMediaTypes.HAL_JSON)
    public Response getHome() {
        URI baseUri = uriInfo.getBaseUri();
        return Response.ok(
                rf.newRepresentation(baseUri)
                        .withLink(
                                LinkRelations.REL_SEARCH,
                                UriBuilder.fromUri(baseUri).path(BookResource.class).replaceQuery("q").build()
                                        .toString()
                                        + "={query}")
                        .withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF))

        .build();
    }

    // .cacheControl(defaultCacheControl()) // TODO Caching
    private CacheControl defaultCacheControl() {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(300);
        return cc;
    }
}

package de.saxsys.hypermedia.rest.resource;

import static de.saxsys.hypermedia.rest.hal.HalMediaTypes.HAL_JSON;

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

import org.jboss.resteasy.annotations.GZIP;

import com.theoryinpractise.halbuilder.api.RepresentationFactory;

import de.saxsys.hypermedia.rest.LinkRelations;

@RequestScoped
@Path("/")
@GZIP
public class HomeResource {

    @Inject
    private RepresentationFactory rf;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(HAL_JSON)
    public Response getHome() {
        URI baseUri = uriInfo.getBaseUri();
        return Response.ok(
                rf.newRepresentation(baseUri)
                        .withLink(
                                LinkRelations.REL_SEARCH,
                                UriBuilder.fromUri(baseUri)
                                        .path(BookResource.class)
                                        .build()
                                        .toString()
                                        + "?q={query}")
                        .withNamespace(LinkRelations.NAMESPACE, LinkRelations.NAMESPACE_HREF))
                .cacheControl(cc())
                .build();
    }

    private CacheControl cc() {
        CacheControl cc = new CacheControl();
        cc.setMaxAge(10); // 10 seconds
        return cc;
    }
}

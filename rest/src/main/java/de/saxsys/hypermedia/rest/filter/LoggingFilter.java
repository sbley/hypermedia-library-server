package de.saxsys.hypermedia.rest.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Integer.MIN_VALUE)
public class LoggingFilter implements ContainerResponseFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException {
        LOGGER.info("{} {} {}", requestContext.getMethod(), requestContext.getUriInfo()
                .getRequestUri(), responseContext.getStatus());
    }

}

package de.saxsys.hypermedia.rest.filter;

import java.io.IOException;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Integer.MIN_VALUE)
@PreMatching
public class LoggingFilter implements ContainerRequestFilter {

    private static Logger LOGGER = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        LOGGER.info("Received request {} {}", context.getMethod(), context.getUriInfo()
                .getRequestUri());
    }

}

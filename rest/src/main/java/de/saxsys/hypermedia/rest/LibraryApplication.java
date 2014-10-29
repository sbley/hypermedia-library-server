package de.saxsys.hypermedia.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.saxsys.hypermedia.rest.filter.CorsFilter;
import de.saxsys.hypermedia.rest.filter.LoggingFilter;
import de.saxsys.hypermedia.rest.hal.HalBuilderMessageBodyReader;
import de.saxsys.hypermedia.rest.hal.HalBuilderMessageBodyWriter;
import de.saxsys.hypermedia.rest.mapping.exception.DefaultExceptionMapper;
import de.saxsys.hypermedia.rest.mapping.exception.WebApplicationExceptionMapper;
import de.saxsys.hypermedia.rest.resource.BookResource;
import de.saxsys.hypermedia.rest.resource.HomeResource;

@ApplicationPath("/")
public class LibraryApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        // Resources
        classes.add(HomeResource.class);
        classes.add(BookResource.class);
        // Providers
        classes.add(HalBuilderMessageBodyReader.class);
        classes.add(HalBuilderMessageBodyWriter.class);
        classes.add(WebApplicationExceptionMapper.class);
        classes.add(DefaultExceptionMapper.class);
        classes.add(LoggingFilter.class);
        classes.add(CorsFilter.class);
        return classes;
    }
}

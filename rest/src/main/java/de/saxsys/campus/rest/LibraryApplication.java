package de.saxsys.campus.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import de.saxsys.campus.rest.auth.CorsFilter;
import de.saxsys.campus.rest.hal.HalBuilderMessageBodyReader;
import de.saxsys.campus.rest.hal.HalBuilderMessageBodyWriter;
import de.saxsys.campus.rest.mapping.exception.DefaultExceptionMapper;
import de.saxsys.campus.rest.mapping.exception.WebApplicationExceptionMapper;
import de.saxsys.campus.rest.resource.BookResource;
import de.saxsys.campus.rest.resource.HomeResource;

@ApplicationPath("/")
public class LibraryApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        classes.add(HomeResource.class);
        classes.add(BookResource.class);
        classes.add(HalBuilderMessageBodyReader.class);
        classes.add(HalBuilderMessageBodyWriter.class);
        classes.add(CorsFilter.class);
        classes.add(WebApplicationExceptionMapper.class);
        classes.add(DefaultExceptionMapper.class);
        return classes;
    }
}

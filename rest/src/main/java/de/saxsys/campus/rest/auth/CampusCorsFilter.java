package de.saxsys.campus.rest.auth;

import java.util.Arrays;
import java.util.HashSet;

import javax.ws.rs.container.PreMatching;

import org.jboss.resteasy.plugins.interceptors.CorsFilter;

@PreMatching
public class CampusCorsFilter extends CorsFilter {

	public CampusCorsFilter() {
		this.allowedOrigins = new HashSet<String>(Arrays.asList("*"));
	}

	// @Override
	// public Set<String> getAllowedOrigins() {
	// return new HashSet<String>(Arrays.asList("*"));
	// }

}

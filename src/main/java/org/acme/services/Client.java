package org.acme.services;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
@RegisterRestClient(baseUri = "stork://guitar-hero-service")
public interface Client {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String invoke();
}
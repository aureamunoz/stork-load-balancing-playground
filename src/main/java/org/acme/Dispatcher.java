package org.acme;

import org.acme.services.Client;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/dispatcher")
public class Dispatcher {

    @RestClient
    Client client;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String invoke() {
        return client.invoke();
    }


}
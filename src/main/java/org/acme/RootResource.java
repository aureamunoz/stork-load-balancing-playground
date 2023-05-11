package org.acme;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class RootResource {

    @Inject
    Template index;

    @ConfigProperty(name = "quarkus.stork.guitar-hero-service.load-balancer.type", defaultValue = "round-robin")
    String lbStrategy;
    @ConfigProperty(name = "quarkus.stork.guitar-hero-service.service-discovery.type", defaultValue = "round-robin")
    String sdStrategy;

    @GET
    @Produces(MediaType.TEXT_HTML)
    public TemplateInstance index() {

        StorkConfig strategies = new StorkConfig(sdStrategy, lbStrategy);
        return index.data("strategies",strategies);
    }

    public static class StorkConfig {
        public String sd;
        public String lb;

        public StorkConfig(String sd, String ld) {
            this.sd = sd;
            this.lb = ld;
        }

        public String getSd() {
            return sd;
        }

        public String getLb() {
            return lb;
        }
    }
}

package org.acme.services;

import io.quarkus.runtime.StartupEvent;
import io.vertx.mutiny.core.Vertx;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Random;

@ApplicationScoped
public class Services {

    @ConfigProperty(name = "slash.http-port", defaultValue = "9000")
    int slashPort;

    @ConfigProperty(name = "slash.delay-in-ms", defaultValue = "300")
    int slashDelay;

    @ConfigProperty(name = "hendrix.http-port", defaultValue = "9001")
    int hendrixPort;

    @ConfigProperty(name = "hendrix.delay-in-ms", defaultValue = "500")
    int hendrixDelay;

    @ConfigProperty(name = "eddie.http-port", defaultValue = "9002")
    int eddiePort;

    @ConfigProperty(name = "eddie.failure-ratio", defaultValue = "30")
    int eddieFailureRatio;

    public void init(@Observes StartupEvent ev, Vertx vertx, Logger logger) throws IOException, URISyntaxException {

        var jHendrix = Files
                .readAllBytes(new File(getClass().getResource("/META-INF/resources/JimiHendrix.jpg").toURI()).toPath());
        var jEddie = Files.readAllBytes(
                new File(getClass().getResource("/META-INF/resources/EddieVanHalen.jpg").toURI()).toPath());
        var jSlash = Files
                .readAllBytes(new File(getClass().getResource("/META-INF/resources/Slash.jpg").toURI()).toPath());
        var jQuarkus = Files
                .readAllBytes(new File(getClass().getResource("/META-INF/resources/quarkus.jpg").toURI()).toPath());
        Random random = new Random();
        vertx.createHttpServer().requestHandler(req -> {
            vertx.setTimer(slashDelay, x -> {
                req.response().endAndForget(Base64.getEncoder().encodeToString(jSlash));
            });
        }).listenAndAwait(slashPort);

        vertx.createHttpServer().requestHandler(req -> {
            vertx.setTimer(hendrixDelay, x -> {
                req.response().endAndForget(Base64.getEncoder().encodeToString(jHendrix));
            });
        }).listenAndAwait(hendrixPort);

        vertx.createHttpServer().requestHandler(req -> {
            vertx.setTimer(5, x -> {
                if (random.nextInt(100) > (100 - eddieFailureRatio)) {
                    req.response().endAndForget(Base64.getEncoder().encodeToString(jEddie));
                } else {
                    req.response().endAndForget(Base64.getEncoder().encodeToString(jQuarkus));
                }
            });
        }).listenAndAwait(eddiePort);

        logger.infof("""
                Services Started:
                    - Slash -> port: %d, delay: %dms
                    - Jimi Hendrix -> port: %d, delay: %dms
                    - Eddie Van Halen -> %d, failure ratio: %s
                """, slashPort, slashDelay, hendrixPort, hendrixDelay, eddiePort, eddieFailureRatio + "%");
    }

}
